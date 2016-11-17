import random
# generate sample_data.sql

# create 200 users
# each user needs the following:
#	cid varchar(9)
#	salutation
# fname
# lname
# credit-card #
#	credit-card exp. date
# street
# city
# state
# phone
# email
# frequent_miles (airline id)

ids = []
title = ['Mr', 'Mrs', 'Ms']
fname = ('Jim', 'John', 'Janet', 'Greg', 'James', 'Bill', 'William',
					'Albert', 'Jacob', 'Marge', 'Anita', 'Rachel', 'Ashley', 'Tammy',
					'Paul', 'Justin', 'Frank', 'Eric', 'Bryce', 'Adam', 'Jenna', 'Abby',
					'Jesse', 'Jessica', 'Allison', 'Ally', 'Lauren', 'Lindsay')
lname = ('Johnson', 'Smith', 'Jones', 'Hardy', 'Ward', 'Johnston', 'Sullivan',
					'Crosby', 'Malkin', 'Peters', 'Palmer', 'Woods', 'Nicholson',
					'Bates', 'Williams', 'Taylor', 'Burns', 'Brown', 'Miller', 'Davis',
					'Jackson', 'Thompson')
	
card_num = [] # should be rand 16 digits

exp_date = ('1-sep-2017', '1-sep-2018', '1-sep-2019')

st = ('Main', 'Church', 'Washington', 'Jefferson', 'Fifth', 'Forbes')

city = ('Pittsburgh', 'New York', 'Boston', 'Chicago')

state = ('PA', 'NY', 'MA', 'IL')

#phone should be random 10 digits

# email will be first_char first name, last name, @gmail.com

# create table Customer(
	# cid varchar2(9) not null,
	# salutation varchar2(3),
	# first_name varchar2(30) not null,
	# last_name varchar2(30) not null,
	# credit_card_num varchar2(16), -- add constraint to check valid credit card #
	# credit_card_expire date,
	# street varchar2(30),
	# city varchar2(30),
	# state varchar2(2),
	# phone varchar2(10),
	# email varchar2(30),
	# frequent_miles varchar2(5), --references Airline.airline_id --> should it be fk?
	# constraint customer_pk primary key (cid),
	# constraint salutation_chk check ((salutation in('Mr', 'Mrs', 'Ms')) or (salutation is null))
# );

frequent = 'NULL'

print('Creating insertion script\n')

script = open('sample-data.sql', 'w')
print(script.name + '\nwriting sql statements to script\n')

script.write('--Customer sample data\n\n')
for i in range(0,250):
	ids.append(random.randint(100000000, 999999999))
	card_num.append(random.randint(1000000000000000, 9999999999999999))
	strn = 'insert into Customer values (\'' + str(ids[len(ids)-1]) + '\',\'' + title[i%len(title)] + '\',\''
	str2 = fname[i%len(fname)] + '\',\'' + lname[i%len(lname)] + '\', ' + '\'' + str(card_num[len(card_num)-1]) +'\''
	str3 = ',\'' + exp_date[i%len(exp_date)] + '\',\'' + st[i%len(st)] + '\', \'' + city[i%len(city)]
	
	str4 = '\', \'' + state[i%len(state)] + '\', \'' + str(random.randint(1000000000, 9999999999)) + '\', \'' + fname[i%len(fname)] + '.' + lname[i%len(lname)] + '@gmail.com\', NULL);'

	script.write(strn + str2 + str3 + str4 +'\n')

print('Customers added to script\nNow creating reservations\n')

# create Reservations

# create table Reservation(
	# reservation_number varchar2(5) not null,
	# cid varchar2(9) not null,
	# cost int,
	# credit_card_num varchar2(16),
	# reservation_date date,
	# ticketed varchar2(1),
	# constraint reservation_pk primary key (reservation_number),
	# constraint reservation_fk foreign key (cid) references Customer (cid)
# );

# resNum = random.randint(10000, 99999)
# pull rand cid from ids list
# cost = randint(5000)
# pull card num from assoc. user id
mon = ('jan', 'feb', 'mar', 'apr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec')
# day is rand int between 1, 28

yr = ('2016', '2017', '2018', '2019')

# res_date will be a combo of these 3 ^

tix = ('Y', 'N')

cities = ('NY', 'PIT', 'BOS', 'CHI', 'LA', 'MIL')


script.write('\n\n--Reservation sample data\n\n')
res_nums = []
for x in range(0,350):
	resNum = random.randint(10000, 99999)
	res_nums.append(resNum)
	# id = random.randint(0, len(ids)-1)
	cost = random.randint(0, 5000)
	mth = mon[random.randint(0,11)]
	day = random.randint(1,28)
	year = yr[random.randint(0,3)]
	
	resy = 'insert into Reservation values (\'' + str(resNum) + '\', \'' + str(ids[x%len(ids)]) + '\', \'' + str(cost) + '\', \'' + str(card_num[x%len(ids)]) + '\', \'' + str(day) + '-' + mth + '-' + year
	resy2 = '\', \'' + tix[random.randint(0,1)] + '\', \''+ cities[x%len(cities)] + '\', \'' + cities[(x+1)%len(cities)] + '\');'

	script.write(resy + resy2 + '\n')
	

print('Reservations added to script\n')
print('Now adding 10 airlines\n')

# create table Airline(
	# airline_id varchar2(5) not null,
	# airline_name varchar2(50) not null,
	# airline_abbreviation varchar2(10) not null,
	# year_founded int,
	# constraint airline_pk primary key (airline_id)
# );

air = 'insert into Airline values ('

script.write('\n\n--Airline sample data\n\n')

script.write(air + '\'001\', \'United Airlines\', \'UAL\', 1931);\n')
script.write(air + '\'002\', \'Delta Airlines\', \'DAL\', 1931);\n')
script.write(air + '\'003\', \'American Airlines\', \'AME\', 1931);\n')
script.write(air + '\'004\', \'Northwest Airlines\', \'NWA\', 1931);\n')
script.write(air + '\'005\', \'Southwest Airlines\', \'SWA\', 1931);\n')
script.write(air + '\'006\', \'Fronteir Airlines\', \'FAL\', 1931);\n')
script.write(air + '\'007\', \'Malaysia Airlines\', \'MAL\', 1931);\n')
script.write(air + '\'008\', \'Emirates Airlines\', \'EAL\', 1931);\n')
script.write(air + '\'009\', \'Alaska Airlines\', \'AAL\', 1931);\n')
script.write(air + '\'010\', \'JetBlue\', \'JTB\', 1931);\n')

print('Airlines Created\n')
print('Creating 30 sample planes\n')

script.write('\n\n--Plane sample data\n\n')

pln = ('Cessna 172', 'Piper PA-28', 'Cessna 150', 'Cessna 182', 'Piper J-3 Cub', 
'Antanov An-2', 'Beechcraft Bonanza')

owner = ('001', '002', '003', '004', '005', '006', '007', '008', '009', '010')

# create table Plane(
	# plane_type char(4) not null,
	# manufacture varchar2(10),
	# plane_capacity int,
	# last_service_date date,
	# year int,
	# owner_id varchar2(5) not null,
	# constraint plane_pk primary key (plane_type),
	# constraint plane_fk foreign key (owner_id) references Airline (airline_id)
# );
plane = 'insert into Plane values ('
for i in range(0,30):
	mth = mon[random.randint(0,11)]
	day = random.randint(1,28)
	year = random.randint(1985,2010)
	script.write(plane + '\'C0' + str(i) + '\', \'Cessna\', ' + str(random.randint(50,250)) + ', \'' + str(day) + '-' + mth + '-' + str(year) + '\', ' + str(random.randint(1965,2010)) + ', \'' + owner[i%len(owner)] + '\');\n')

print('Planes created\n')

script.write('\n\n--Flight sample data\n\n')

# create table Flight(
	# flight_number varchar2(3) not null,
	# airline_id varchar2(5) not null,
	# plane_type char(4) not null,
	# departure_city varchar2(3),
	# arrival_city varchar2(3),
	# departure_time varchar2(4),
	# arrival_time varchar2(4),
	# weekly_schedule varchar2(7),
	# constraint flight_pk primary key (flight_number),
	# constraint flt_airline_fk foreign key (airline_id) references Airline (airline_id),
	# constraint flt_plane_fk foreign key (plane_type) references Plane (plane_type)
# );

print('Creating 100 flights\n')

print('cities length: ' + str(len(cities)))
sched = 'SMTWTFS'
for i in range(0,100):
	flt_num = i
	a_id = (i%10)+1
	p_tp = i%30
	d_t = random.randint(100,2359)
	a_t = d_t + random.randint(100,800)
	
	x = i + 1
	if a_id < 10:
		script.write('insert into Flight values (\'' + str(i) + '\', \'00' + str(a_id) + '\', \'C0' + str(p_tp) + '\', \'' + cities[i%len(cities)] + '\', \'' + cities[x%len(cities)] + '\', \'' + str(d_t) + '\', \'' + str(a_t) + '\', \'' + sched + '\');\n')
	else:
		script.write('insert into Flight values (\'' + str(i) + '\', \'0' + str(a_id) + '\', \'C0' + str(p_tp) + '\', \'' + cities[i%len(cities)] + '\', \'' + cities[x%len(cities)] + '\', \'' + str(d_t) + '\', \'' + str(a_t) + '\', \'' + sched + '\');\n')

print('Flights created\n')


# Write reservation details to script

# create table Reservation_detail(
	# reservation_number varchar2(5),
	# flight_number varchar2(3),
	# flight_date date,
	# leg int,
	# constraint res_detail_pk primary key (reservation_number, leg),
	# constraint res_detail_fk foreign key (reservation_number) references Reservation (reservation_number),
	# constraint res_det_flt_fk foreign key (flight_number) references Flight (flight_number)
# );

print('Creating 300 reservation details\n')
script.write('\n\n--Reservation Detail sample data\n\n')
year = '2017'
for i in range(0,350):
	flt_num = i%100
	mth = mon[random.randint(0,11)]
	day = random.randint(1,28)
	
	script.write('insert into Reservation_detail values (\'' + str(res_nums[i]) + '\', \'' + str(flt_num) + '\', \'' + str(day) + '-' + mth + '-' + year + '\', ' + str(1) + ');\n')
	
print('300 resy detail records generated\n')

print('Generating Price data\n')

# -- Price table
# -- assumes only one flight serves each route
# create table Price(
	# departure_city char(3),
	# arrival_city char(3),
	# airline_id varchar(5) not null,
	# high_price int,
	# low_price int,
	# constraint price_pk primary key (departure_city, arrival_city),
	# constraint price_fk foreign key (airline_id) references Airline (airline_id)
# );

script.write('\n\n--Price sample data\n\n')
for i in range(0,150):
	d_city = cities[i%5]
	a_city = cities[(i+1)%5]
	
	airln = (i%10)+1
	hi_p = random.randint(50,1000)
	lo_p = hi_p/2
	
	script.write('insert into Price values (\'' + d_city + '\', \'' + a_city + '\', \'' + str(airln) + '\', ' + str(hi_p) + ', ' + str(lo_p) + ');\n')

script.close()