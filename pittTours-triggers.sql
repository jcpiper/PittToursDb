-- triggers

--create adjustTicket trigger


-- create planeUpgrade trigger

-- create cancelReservation trigger


create or replace procedure switch_plane(flightNo in varchar2(3), newPlane in char(4))
	is
	begin
		update flight
		set plane_type = newPlane
		where flight_number = flightNo;
		
		commit;
	end;
	/

