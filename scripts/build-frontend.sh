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

cd ../SocialPostingFrontend

echo -e "Frontend: Building..."
echo -n "${yellow}"
npm run build > /dev/null
echo -n "${reset}"
handleOutput
echo -e "Frontend: ${green}Done${reset}"
