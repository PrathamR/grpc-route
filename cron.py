import os
import subprocess
import sys
os.system("sudo arp -a -d")
arg = str(sys.argv[1])
os.system("nmap -sP "+ arg)
proc = subprocess.check_output("arp -a", shell=True)
ip_list = subprocess.check_output("ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1'", shell=True)
my_ips = ip_list.split()
my_ip = ""
for ip in my_ips:
    if arg[:4] in ip:
        my_ip = ip
print "my ip is ", my_ip
lines = proc.split('\n')
text_file = open("./resources/ips.txt", "w")
for line in lines:
    if "incomplete" not in line and my_ip not in line and arg[:5] in line:
        ip = line[line.find("(")+1:line.find(")")]
        print ip
        text_file.write(ip+"\n")
