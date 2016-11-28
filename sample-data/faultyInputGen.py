# Script to generate large sample file with one error in the last line for testing the loadAirline method in the admin interface

file = open('bad-air.csv', 'w')

for x in range(0,150000):
	file.write('001,United Airlines,UAL,Chicago,1931\n')

file.write('003,001,United Airlines,UAL,Chicago,1931')

file.close()