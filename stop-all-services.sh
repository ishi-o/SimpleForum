#!/bin/bash
ps -ef | grep -E "api-gateway|content-service|user-service" | grep -v grep | awk '{print $2}' | xargs -r kill -9

cd "$PWD/docker" && docker-compose down
