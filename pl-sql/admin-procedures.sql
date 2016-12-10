-- Procedures for admin interface

-- procedure to erase all tuples in all tables
create or replace procedure eraseDb
	as
	begin
		delete from Reservation_detail;
		delete from Reservation;
		delete from Plane;
		delete from Flight;
		delete from Price;		
		delete from Customer;
		delete from Airline;
		delete from Sys_time;
	end;
	/

create or replace procedure loadAirline(airId in airline.airline_id%TYPE, name in airline.airline_name%TYPE, abbr in airline.airline_abbreviation%TYPE, yr in airline.year_founded%TYPE)
	as
	begin
		insert into airline values (airId, name, abbr, yr);
	end;
	/

create or replace procedure loadSchedule(fltNum in varchar2, airId in varchar2, planeType in varchar2, dCity in varchar2, aCity in varchar2, dTime in varchar2, aTime in varchar2, sched in varchar2)
	as
	begin
		insert into flight values (fltNum, airId, planeType, dCity, aCity, dTime, aTime, sched);
	end;
	/
	
create or replace procedure loadPricing(dCity in varchar2, aCity in varchar2, airId in varchar2, hiPrice in int, loPrice in int)
	as
	begin
		insert into price values (dCity, aCity, airId, hiPrice, loPrice);
	end;
	/
	
create or replace procedure changePrice(dCity in price.departure_city%TYPE, aCity in price.arrival_city%TYPE, hiPrice in int, loPrice in int)
	as
	begin
		update price
		set low_price = loPrice, high_price = hiPrice
		where departure_city = dCity and arrival_city = aCity;
	end;
	/

create or replace procedure loadPlane(plnType in char, manu in varchar2, capacity in int, serviceDate in date, yr in int, airline in varchar2)
	as
	begin
		insert into plane values (plnType, manu, capacity, serviceDate, yr, airline);
	end;
	/
	
-- select customer info from join of customer, reservation, res_detail
create or replace view passengers_on_flight as
	select c.salutation, c.first_name, c.last_name, d.flight_date, d.flight_number
	from customer c, reservation r, reservation_detail d
	where r.reservation_number = d.reservation_number and c.cid = r.cid;
	
-- select flights that have at least 1 identical day in schedule
-- schedule of the form 'SMTWTFS'
-- create or replace view same_day_flights as
	-- select f.*, s.* from flight f, flight s
	-- where (f.weekly_schedule like 'S%' and s.weekly_schedule like 'S%') or
		-- (f.weekly_schedule like '-M%' and s.weekly_schedule like '-M%') or
		-- (f.weekly_schedule like '--T%' and s.weekly_schedule like '--T%') or
		-- (f.weekly_schedule like '%W%' and s.weekly_schedule like '%W%') or
		-- (f.weekly_schedule like '----T--' and s.weekly_schedule like '----T--') or
		-- (f.weekly_schedule like '%F-' and s.weekly_schedule like '%F-') or
		-- (f.weekly_schedule like '------S' and s.weekly_schedule like '------S');

create or replace view sunday_flights as
	select * from flight
	where weekly_schedule like 'S%';

create or replace view monday_flights as
	select * from flight
	where weekly_schedule like '-M%';
	
create or replace view tuesday_flights as
	select * from flight
	where weekly_schedule like '--T%';
	
create or replace view wednesday_flights as
	select * from flight
	where weekly_schedule like '%W%';

create or replace view thursday_flights as
	select * from flight
	where weekly_schedule like '----T--';
	
create or replace view friday_flights as
	select * from flight
	where weekly_schedule like '%F-';
	
create or replace view saturday_flights as
	select * from flight
	where weekly_schedule like '%S';
	
-- create or replace view same_day_flights as
	-- select f.flight_number, s.flight_number
	-- from flight f, flight s
	-- where 
