import os
import subprocess
import sys
import time
from threading import Thread

DEBUG = True
devnull = open(os.devnull, 'w')

live_ips = {}
my_ips = []

def check_ip_status():
	global live_ips
	while(True):
		text_file = open("./resources/ips.txt", "w")
		for key in live_ips.keys():
			proc_output = subprocess.call("ping -c 1 -W 0.1 %s"%key,stdout=devnull, stderr=subprocess.STDOUT,shell=True)
			if DEBUG:
				print "inside check_ip_status ip : {} status : {}".format(key, proc_output)
			if proc_output == 0:
				live_ips[key] = True
				text_file.write(key + '\n')
			else:
				live_ips[key] = False
		text_file.close()
		time.sleep(2)

def monitor_ips():
	global live_ips
	global my_ips
	while(True):
		proc_output = subprocess.check_output("arp -a", shell=True)
		lines = proc_output.split('\n')
		for line in lines:
		    ip = line[line.find("(")+1:line.find(")")]
		    if ip not in live_ips.keys() and ip not in my_ips and ip is not '':
		    	live_ips[ip] = False
		    	if DEBUG:
		    		print "inside monitor ips. Newly added ip is: {}".format(ip)
		time.sleep(10)

def get_my_ips():
	global my_ips
	ip_list = subprocess.check_output("ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1'", shell=True)
	my_ips = ip_list.split()
	if DEBUG:
		print "Inside get my ips. My device ip list is ", my_ips

if __name__=='__main__':
	get_my_ips()
	Thread(target = monitor_ips).start()
	time.sleep(1)
	Thread(target = check_ip_status).start()
