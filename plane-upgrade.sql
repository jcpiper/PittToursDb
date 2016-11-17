-- Trigger 2
create or replace function get_next_plane (currPlane in int) return char
	as
	nextPlane char(4);
	begin
		select plane_type into nextPlane
		from plane
		where plane_capacity > currPlane
		order by plane_capacity
		fetch first row only;
	
		
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
create or replace procedure change_plane_type (currPlane in plane.plane_capacity%TYPE, flightNo in flight.flight_number%TYPE)
	as
	
	nxtPlane char(4) := get_next_plane(currPlane);
	nxtAirline varchar2(5) := get_next_plane_owner(currPlane);
	begin
		set transaction read write name 'update_plane_for_flight';
		-- set constraints all deferred;
		
		update flight
		set plane_type = nxtPlane
		where flight_number = flightNo;
		
		update flight
		set airline_id = nxtAirline
		where flight_number = flightNo;
		
		commit;
	end;
	/
show errors;