-- triggers



-- procedure to switch the plane for a given flight
create or replace procedure switch_plane(flightNo in flight.flight_number%TYPE, newPlane out plane.plane_type%TYPE, cap out plane.plane_capacity%TYPE, newCap out plane.plane_capacity%TYPE)--, newPlane in flight.plane_type%TYPE)
	is
	begin
		select plane_type into newPlane
		from flight_options
		where flight_number = flightNo;
		
		select plane_capacity into cap
		from plane p, flight f
		where p.plane_type = f.plane_type and p.owner_id = f.airline_id and f.flight_number = flightNo;
		
		select "Capacity" into newCap
		from flight_options
		where flight_number = flightNo;
		
		if cap < newCap then
			update flight
			set plane_type = newPlane
			where flight_number = flightNo;
		end if;
		commit;
	end;
	/
show errors;
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
create or replace view reservations_per_flight as
	select f.flight_number, count(*) as reservations
	from flight f, reservation r, reservation_detail d
	where d.flight_number = f.flight_number and r.reservation_number = d.reservation_number and r.ticketed = 'Y'
	group by f.flight_number
	order by reservations;
	
	
--get all planes that can hold the ticketed reservations for a given flight

create or replace view flight_options as
	select min(p.plane_capacity) "Capacity", r.flight_number, p.plane_type
	from plane p, reservations_per_flight r
	where p.plane_capacity > r.reservations
	group by r.flight_number, p.plane_type;


-- create or replace view test_view as
-- select count(*) as resys
-- from flight f join reservation r on
-- (f.departure_city = r.start_city and f.arrival_city = r.end_city)
-- where r.ticketed = 'Y'
-- group by f.flight_number;
