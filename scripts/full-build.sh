#!/bin/bash

cd "$(dirname "${BASH_SOURCE[0]}")"

echo "Progress:"
source ./build-microservices.sh
if [ $? != 0 ] ; then
	exit 1
fi
source ./build-frontend.sh
