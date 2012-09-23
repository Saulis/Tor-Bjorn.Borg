#!/bin/sh
kill `ps -ef | awk '/tor-bjorn.borg/{ print $2 }'`
