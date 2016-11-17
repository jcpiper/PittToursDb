-- triggers



-- procedure to switch the plane for a given flight
create or replace procedure switch_plane(flightNo in flight.flight_number%TYPE)--, newPlane in flight.plane_type%TYPE)
	is
	-- declare
		newPlane plane.plane_type%TYPE;
		cap plane.plane_capacity%TYPE;
		newCap plane.plane_capacity%TYPE;
	begin
		-- assign the plane type with minimum viable capacity to var newPlane

		newPlane := get_alternate_plane(flightNo);

		dbms_output.Put_line(newPlane); --display value

		-- assign the capacity of the current plane being used by this flight to var cap
		select plane_capacity into cap
		from plane p, flight f
		where p.plane_type = f.plane_type and p.owner_id = f.airline_id and f.flight_number = flightNo;

		-- assign capacity of newPlane to var newCap
		select plane_capacity into newCap
		from plane
		where plane_type = newPlane;

		-- if capacity of newPlane is lower than that of the original plane, make newPlane the plane for the flight
			if cap < newCap then
			update flight
			set plane_type = newPlane
			where flight_number = flightNo;
		end if;
		commit;
	end;
	/
show errors;

-- delete non ticketed reservations for flight flightNo
create or replace procedure delete_reservations(flightNo in reservation_detail.flight_number%TYPE)
	is
	begin
		delete from reservation
		where ticketed = 'N' and reservation_number in (select r.reservation_number
																										from reservation r join reservation_detail d
																										on r.reservation_number = d.reservation_number
																										where d.flight_number = flightNo
																									);
		commit;
	end;
	/

--need to find number of ticketed reservations for a given flight
--no need to make sure ticketed is 'Y', because non ticketed reservations have been deleted by this point
create or replace view reservations_per_flight as
	select f.flight_number, count(*) as reservations
	from flight f, reservation r, reservation_detail d
	where d.flight_number = f.flight_number and r.reservation_number = d.reservation_number
	group by f.flight_number
	order by reservations;


--get all planes that can hold the ticketed reservations for a given flight
-- function to get plane that can hold reservations

create or replace function get_alternate_plane (flightNo in varchar2) return char
	as
	resys int;
	pln char(4);
	begin
		select reservations into resys
		from reservations_per_flight
		where flight_number = flightNo;

		select plane_type into pln
		from plane
		where plane_capacity > resys
		order by plane_capacity
		fetch first row only;

		return (pln);
	end;
	/
	show errors;

create or replace procedure check_flights(sysTime in date)
	as
		cursor flt_cursor is
			select flight_number
			from flight
			where departure_time <= sysTime + 12/24;

		flt flight.flight_number%TYPE;
	begin
		open flt_cursor;
		loop
			fetch flt_cursor into flt;
			delete_reservations(flt);
			switch_plane(flt);

			exit when flt_cursor%NOTFOUND;
		end loop;
	end;
	/
show errors;
-- Trigger 3
-- assumes sys-time is updated every hour on the hour
create or replace trigger cancelReservation
	after insert on Sys_time

	for each row
	begin
		check_flights(:new.c_date);
	end;
	/
show errors;



-- Returns number of reservations on the same flight as a given reservation num
create or replace function resOnFlight(flightNum in varchar2)
  return int
  is
  this_flight varchar2(3);
  on_flight int;
  begin
    select count(*) into on_flight
    from Reservation_detail
    where flight_number = flightNum;

    return on_flight;
  end;
/

-- Trigger adjustTicket
create or replace trigger adjustTicket
  after update of high_price, low_price
  on Price
  for each row
begin
  update Reservation
  set cost =  case
                when cost = :old.low_price and ticketed = 'N' then
                  :new.low_price
                when cost = :old.high_price and ticketed = 'N' then
                  :new.high_price
                else
                  111
              end
  where :new.departure_city = start_city and :new.arrival_city = end_city;
end;
/

set constraints flt_airline_fk, flt_plane_fk deferred;

-- Trigger planeUpgrade
create or replace trigger planeUpgrade
  before insert on Reservation
  for each row
  declare
    thisFlightNum varchar2(3);
    numResThisFlight int;
    planeTypeThisFlight char(4);
    airlineIdThisFlight varchar2(5);
    planeCapThisFlight int;
    isLargestPlane int;
    flightFull EXCEPTION;
  begin
    select flight_number into thisFlightNum from Flight f
    where f.departure_city = :new.start_city AND f.arrival_city = :new.end_city;

    numResThisFlight := resOnFlight(thisFlightNum);

    planeTypeThisFlight := get_plane_type_for_flight(thisFlightNum);

    airlineIdThisFlight := get_airline_for_flight(thisFlightNum);

    select plane_capacity into planeCapThisFlight from Plane
    where plane_type = planeTypeThisFlight AND owner_id = airlineIdThisFlight;

    isLargestPlane := -1;
    isLargestPlane := is_biggest_plane(planeTypeThisFlight);

		--set constraints all deferred;

    if numResThisFlight = planeCapThisFlight AND isLargestPlane = 0
      then
        change_plane_type(planeCapThisFlight, planeTypeThisFlight, thisFlightNum);
    elsif numResThisFlight = planeCapThisFlight AND isLargestPlane = 1
      then
        raise flightFull;
    end if;

  EXCEPTION
    WHEN flightFull THEN
       DBMS_OUTPUT.PUT_LINE('Insert Reservation failed - flight is full.');
  end;
/
show errors;

-- Trigger 2
create or replace function get_next_plane (currPlane in int, currType in char) return char
	as
	nextPlane char(4);
	cap int;
	begin
		select max(plane_capacity) into cap
		from plane;
		if currPlane = cap then
			return currType;
		else
			select plane_type into nextPlane
			from plane
			where plane_capacity > currPlane
			order by plane_capacity
			fetch first row only;
		end if;
		-- if nextPlane is null then --current plane is the largest plane
			-- select plane_type into nextPlane
			-- from plane
			-- where plane_capacity = currPlane and plane_type = currType;
		-- end if;
		return nextPlane;
		-- if nextPlane is null, throw exception
	end;
	/
show errors;

create or replace function get_next_plane_owner (currPlane in int) return varchar2
	as
	owner varchar2(5);
	
	begin
		select owner_id into owner
		from plane
		where plane_capacity > currPlane
		order by plane_capacity
		fetch first row only;
		
		return owner;
	end;
	/
show errors;
-- procedure to change the plane for a flight
-- must set constraints to deffered before hand!
create or replace procedure change_plane_type (currPlane in plane.plane_capacity%TYPE, currType in plane.plane_type%TYPE, flightNo in flight.flight_number%TYPE)
	as
	nxtPlane char(4) := get_next_plane(currPlane, currType);
	nxtAirline varchar2(5);
	begin
		if nxtPlane = currType then --largest plane
			commit;
		else
			nxtAirline := get_next_plane_owner(currPlane);
			update flight
			set plane_type = nxtPlane
			where flight_number = flightNo;
			
			update flight
			set airline_id = nxtAirline
			where flight_number = flightNo;
		end if;
		commit;
	end;
	/
show errors;

-- Get the plane type for a reserved flight
create or replace function get_plane_type_for_flight (resy in varchar2) return char
	as
	plane char(4);
	
	begin
		select plane_type into plane
		from flight
		where flight_number = resy;
	
		return plane;
	end;
	/
show errors;

create or replace function get_airline_for_flight (resy in varchar2) return varchar2
	as 
	airline varchar2(5);
	
	begin
		select airline_id into airline
		from flight
		where flight_number = resy;
		
		return airline;
	end;
	/
show errors;

create or replace function is_biggest_plane (plnType in char) return int
	as
	cap int;
	lrg_cap int;
	
	begin
		select plane_capacity into cap
		from plane
		where plane_type = plnType;
		
		select max(plane_capacity) into lrg_cap
		from plane;
		
		if cap < lrg_cap then
			return 0;
		
		else return 1;
		
		end if;
	end;
	/
	show errors;