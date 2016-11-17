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
		dbms_output.Put_line(cap); --display
		-- assign capacity of newPlane to var newCap
		select plane_capacity into newCap
		from plane
		where plane_type = newPlane;
		
		dbms_output.Put_line(newCap); --display newCap
		-- if capacity of newPlane is lower than that of the original plane, make newPlane the plane for the flight
		
		-- dbms_output.Put_line(cap < newCap); --display result of if statement
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
-- assuming sys-time is updated every hour on the hour
create or replace trigger cancelReservation
	after insert on Sys_time
	
	--time var

	for each row
	begin
		check_flights(:new.c_date);
	end;
	/
show errors;
