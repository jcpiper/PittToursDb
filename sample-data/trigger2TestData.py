import random
# cid = 728383041

script = open('trigger-2-max.sql', 'w')

script.write('--Customer sample data\n\n')
for i in range(0,227):
	resNum = random.randint(10000, 99999)
	# res_nums.append(resNum)
	# id = random.randint(0, len(ids)-1)
	cost = random.randint(0, 5000)
	# mth = mon_str[random.randint(0,11)]
	# day = day_str[random.randint(0,27)]
	# year = yr[random.randint(0,3)]

	resy = 'insert into Reservation values (\'' + str(resNum) + '\', \'728383041\', \'' + str(cost) + '\', \'9999999999999999\', ' + "TO_DATE('2016 10 26','YYYY MM DD')"
	resy2 = ', \'N\', \'NY\', \'PIT\');'

	script.write(resy + resy2 + '\n')
	
	flt_num = i%100
	# mth = mon_str[random.randint(0,11)]
	# day = day_str[random.randint(0,27)]

	script.write('insert into Reservation_detail values (\'' + str(resNum) + '\', \'24\', ' + "TO_DATE('2016 10 26','YYYY MM DD'), " + str(1) + ');\n')
print('sample data generated')

script.close()