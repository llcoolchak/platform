#!/bin/bash
rm ./demorc
rm ./demo.pem
cp -f ../OpenStackInstaller/demorc ./
source ./demorc
euca-add-keypair demo > demo.pem
chmod 0600 demo.pem

euca-authorize default -P tcp -p 22 -s 0.0.0.0/0
euca-authorize default -P tcp -p 80 -s 0.0.0.0/0
euca-authorize default -P tcp -p 8080 -s 0.0.0.0/0
euca-authorize default -P icmp -t -1:-1