#!/bin/bash

red=`tput setaf 1`
green=`tput setaf 2`
yellow=`tput setaf 3`
reset=`tput sgr0`

function handleOutput() {
	if [ $? != 0 ] ; then
		echo -e "${red}Build error${reset}"
		exit 1
	fi
}

cd "$(dirname "${BASH_SOURCE[0]}")"

echo -e "APIGateway: Building..."
echo -n "${yellow}"
mvn -f ../Microservices/APIGateway package > /dev/null
echo -n "${reset}"
handleOutput
echo -e "APIGateway: ${green}Done${reset}"

echo -e "Post microservice: Building..."
echo -n "${yellow}"
mvn -f ../Microservices/PostMicroservice package > /dev/null
echo -n "${reset}"
handleOutput
echo -e "Post microservice: ${green}Done${reset}"

echo -e "User microservice: Building..."
echo -n "${yellow}"
mvn -f ../Microservices/UserMicroservice package > /dev/null
echo -n "${reset}"
handleOutput
echo -e "User microservice: ${green}Done${reset}"
