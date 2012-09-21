#!/bin/sh
mkdir logs -p
java -jar tor-bjorn.borg-1.0.0-SNAPSHOT-standalone.jar $1 $2 $3 > logs/`date +%F`-`date +%H%M%S`_$1_game.log 2>&1 &
