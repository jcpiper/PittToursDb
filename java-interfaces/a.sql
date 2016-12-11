-- Returns number of reservations on the same flight as a given reservation num
create or replace function resOnFlightDate(flightNum in reservation_detail.flight_number%TYPE, flightDate in reservation_detail.flight_date%TYPE)
  return int
  is
  on_flight int;
  begin
    select count(*) into on_flight
    from Reservation_detail
    where flight_number = flightNum and flight_date = flightDate;

    return on_flight;
  end;
/
show errors;

create or replace function get_largest_plane
  return int
	is
	cap int;
	begin

		select max(plane_capacity) into cap
    from plane;

		return cap;
	end;
	/
	show errors;
	
	-- task 6 direct routes
	select f.flight_number, f.departure_city, f.departure_time, f.arrival_time
			from flight f
			where ((select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = '11-SEP-2017')
			and f.departure_city = 'PIT' and f.arrival_city = 'BOS');
		
	--task 6 connection routes
	-- ASSUMES ALL CONNECTION FLIGHTS ARE SAME-DATE (NOT NECESSARILY JUST W/IN 24 HOURS) I.E IF FIRST FLIGHT LANDS AT 11 PM AND CONNECTION LEAVES AT 1AM IT WILL NOT BE RETURNED
	-- BY THE QUERY
	select f.flight_number, f.departure_city, f.departure_time, s.arrival_time
		from flight f, flight s
		where
			f.departure_city = 'PIT' and s.arrival_city = 'BOS' and f.arrival_city = s.departure_city and s.departure_time >= (f.arrival_time + 100) and
			(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = '11-SEP-2017')
			and (select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = s.flight_number and flight_date = '11-SEP-2017');
			
--task 7 direct routes
select f.flight_number, f.departure_city, f.departure_time, f.arrival_time
	from flight f join airline a on f.airline_id = a.airline_id
	where f.departure_city = 'PIT' and f.arrival_city = 'BOS' and a.airline_name = 'JetBlue'
	and ((select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = '11-SEP-2017'));

	--task 7 connection routes
	-- ASSUMES ALL CONNECTION FLIGHTS ARE SAME-DATE (NOT NECESSARILY JUST W/IN 24 HOURS) I.E IF FIRST FLIGHT LANDS AT 11 PM AND CONNECTION LEAVES AT 1AM IT WILL NOT BE RETURNED
	-- BY THE QUERY
	select f.flight_number, f.departure_city, f.departure_time, s.arrival_time
		from flight f, flight s, airline a
		where
			f.departure_city = 'PIT' and s.arrival_city = 'BOS' and f.arrival_city = s.departure_city and a.airline_name='JetBlue' and s.departure_time >= (f.arrival_time + 100) and
			f.airline_id = a.airline_id and s.airline_id = a.airline_id and
			(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = '11-SEP-2017')
			and (select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = s.flight_number and flight_date = '11-SEP-2017');
