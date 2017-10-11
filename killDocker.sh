#!/usr/bin/env bash
docker stop $(docker  ps  -a -q)
docker rm  $( docker ps -a -q )
docker rmi -f $(docker images -q -a )
docker volume rm $(docker volume ls -qf dangling=true)
