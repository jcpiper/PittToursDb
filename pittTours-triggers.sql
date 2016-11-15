-- triggers



-- procedure to switch the plane for a given flight
create or replace procedure switch_plane(flightNo in flight.flight_number%TYPE, newPlane in flight.plane_type%TYPE)
	is
	begin
		update flight
		set plane_type = newPlane
		where flight_number = flightNo;
		
		commit;
	end;
	/
	
create or replace procedure delete_reservations(flightNo in flight.flight_number%TYPE)
	is
	begin
		delete from reservation
		where ticketed = 'N' and reservation_number in (select r.reservation_number 
																										from reservation r join flight f 
																										on r.start_city = f.departure_city and r.end_city = f.arrival_city
																										where f.flight_number = flightNo
																									);
		commit;
	end;
	/

--need to find number of ticketed reservations for a given flight
create or replace view reservations_per_flight as
	select f.flight_number as flightNo, count(*) as reservations
	from flight f, reservation r
	where f.departure_city = r.start_city and f.arrival_city = r.end_city and r.ticketed = 'Y'
	group by f.flight_number
	order by reservations;
	
	
--get all planes that can hold the ticketed reservations for a given flight

create or replace view flight_options as
select * from plane p, reservations_per_flight r
where p.plane_capacity < r.reservations;
-- create or replace view test_view as
-- select count(*) as resys
-- from flight f join reservation r on
-- (f.departure_city = r.start_city and f.arrival_city = r.end_city)
-- where r.ticketed = 'Y'
-- group by f.flight_number;
