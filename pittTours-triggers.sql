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

--get all flights w/ planes that can hold 