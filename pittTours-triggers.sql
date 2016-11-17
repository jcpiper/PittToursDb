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
		select plane_type into newPlane
		from flight_options
		fetch first row only;

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

create or replace view flight_options as
	select min(p.plane_capacity) "Capacity", p.plane_type
	from plane p, reservations_per_flight r
	where p.plane_capacity > r.reservations
	group by p.plane_type
	order by "Capacity";


-- create or replace view test_view as
-- select count(*) as resys
-- from flight f join reservation r on
-- (f.departure_city = r.start_city and f.arrival_city = r.end_city)
-- where r.ticketed = 'Y'
-- group by f.flight_number;



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
