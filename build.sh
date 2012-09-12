#!/bin/sh
which lein > /dev/null
if [ "$?" -ne "0" ]; then
echo "leiningen is missing. Please install leiningen."
  exit 1
fi

lein uberjar