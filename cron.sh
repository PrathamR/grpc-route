#!/bin/bash

while true; do
  python cron.py 192.168.1.1/24
  sleep 10;
done
