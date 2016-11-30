-- Customer interface procedures and functions

-- insert a new customer
create or replace procedure addCustomer(title in varchar2, fName in varchar2, lName in varchar2, st in varchar2, cit in varchar2, st in varchar2, 
	phoneNum in varchar2, emailAddress in varchar2, crdNum in varchar2, expDate in date)
	as
	id varchar2;
	begin
		id := getNextCid;
		insert into Customer (cid, salutation, first_name, last_name, credit_card_num, credit_card_expire, street, city, state, phone, email)
		values (id, title, fName, lName, crdNum, expDate, st, cit, st, phoneNum, emailAddress);
	end;
	/
	show errors;
	
-- generate a new unique cid
create or replace function getNextCid return int
	as
		id int;
	begin
		select max(cid) into id
		from Customer;
		
		id := id + 1;
		
		return id;
	end;
	/
	show errors;
	
-- find the low price for a flight
create or replace function getLowPrice(startCity in varchar2, destCity in varchar2) return int
	as
		loPrice int;
	begin	
		select low_price into loPrice
		from price
		where departure_city = startCity and arrival_city = destCity;
		
		return loPrice;
	end;
	/
	
-- find the high price for a flight
create or replace function getHiPrice(startCity in varchar2, destCity in varchar2) return int
	as
		hiPrice int;
	begin
		select high_price into hiPrice
		from price
		where departure_city = startCity and arrival_city = destCity;
		
		return hiPrice;
	end;
	/
	
-- find price of round trip flight
create or replace function getRoundTripPrice(startCity in varchar2, destCity in varchar2) return int
	as
		hiPrice int;
		loPrice int;
		rndPrice int;
	begin
		selselect low_price into loPrice, high_price into hiPrice
		from price
		where departure_city = startCity and arrival_city = destCity;
		
		rndPrice := loPrice + hiPrice;
		
		return rndPrice;
	end;
	/
