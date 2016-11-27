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
show errors;

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