-- PittTours Schema

-- drop preexisting tables
drop table Airline cascade constraints;
drop table Plane cascade constraints;
drop table Flight cascade constraints;
drop table Price cascade constraints;
drop table Customer cascade constraints;
drop table Reservation cascade constraints;
drop table Reservation_detail cascade constraints;
drop table Sys_time cascade constraints;

-- assuming all fields other than year_founded cannot be null
create table Airline(
	airline_id varchar2(5) not null,
	airline_name varchar2(50) not null,
	airline_abbreviation varchar2(10) not null,
	year_founded int,
	constraint airline_pk primary key (airline_id)
);

create table Plane(
	plane_type char(4) not null,
	manufacture varchar2(10),
	plane_capacity int,
	last_service_date date,
	year int,
	owner_id varchar2(5) not null,
	constraint plane_pk primary key (plane_type, owner_id),
	constraint plane_fk foreign key (owner_id) references Airline (airline_id)
);

create table Flight(
	flight_number varchar2(3) not null,
	airline_id varchar2(5) not null,
	plane_type char(4) not null,
	departure_city char(3),
	arrival_city char(3),
	departure_time varchar2(4),
	arrival_time varchar2(4),
	weekly_schedule varchar2(7),
	constraint flight_pk primary key (flight_number),
	constraint flt_airline_fk foreign key (airline_id) references Airline (airline_id),
	constraint flt_plane_fk foreign key (plane_type, airline_id) references Plane (plane_type, owner_id)
);

-- Price table
-- assumes only one flight serves each route
create table Price(
	departure_city char(3),
	arrival_city char(3),
	airline_id varchar(5) not null,
	high_price int,
	low_price int,
	constraint price_pk primary key (departure_city, arrival_city),
	constraint price_fk foreign key (airline_id) references Airline (airline_id)
);


create table Customer(
	cid varchar2(9) not null,
	salutation varchar2(3),
	first_name varchar2(30) not null,
	last_name varchar2(30) not null,
	credit_card_num varchar2(16), -- add constraint to check valid credit card #
	credit_card_expire date,
	street varchar2(30),
	city varchar2(30),
	state varchar2(2),
	phone varchar2(10),
	email varchar2(30),
	frequent_miles varchar2(5), --references Airline.airline_id --> should it be fk?
	constraint customer_pk primary key (cid),
	constraint salutation_chk check ((salutation in('Mr', 'Mrs', 'Ms')) or (salutation is null))
);

-- create Reservation Info table

create table Reservation(
	reservation_number varchar2(5) not null,
	cid varchar2(9) not null,
	cost int,
	credit_card_num varchar2(16),
	reservation_date date,
	ticketed varchar2(1),
	start_city char(3),
	end_city char(3),
	constraint reservation_pk primary key (reservation_number),
	constraint reservation_fk foreign key (cid) references Customer (cid)
	-- constraint reservation_price_fk foreign key (start_city, end_city) references Price (departure_city, arrival_city)
);

-- create reservation_detail table

create table Reservation_detail(
	reservation_number varchar2(5),
	flight_number varchar2(3),
	flight_date date,
	leg int,
	constraint res_detail_pk primary key (reservation_number, leg),
	constraint res_detail_fk foreign key (reservation_number) references Reservation (reservation_number)
);

-- time info table

create table Sys_time(
	c_date date not null,
	constraint date_pk primary key (c_date)
);
