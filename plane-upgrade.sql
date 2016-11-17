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