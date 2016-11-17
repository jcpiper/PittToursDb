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