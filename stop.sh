#!/bin/sh
kill `ps -ef | awk '/[c]lojure-pong/{ print $2 }'`