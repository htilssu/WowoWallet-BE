#!/bin/bash

sudo docker pull "$1"

sudo docker rm -f ewallet || true

sudo docker run --restart always --env-file /env/.env.ewallet --name ewallet -d -p 8080:8080 htilssu/ewallet-springboot:latest

sudo docker image prune -f

