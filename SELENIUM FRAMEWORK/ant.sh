#!/bin/bash

cd "$PWD"
echo $PWD
export PATH=$PATH:$PWD/Resources/ant/bin
export PATH=$PATH:/Library/Java/JavaVirtualMachines/jdk*/Contents/Home
export PATH=$PATH:$PWD/Resources/allure-cli/bin

script="$0"
basename="$(dirname $script)"

cd $basename

ant

mkdir -p "Automation Reports"/Allure/target/allure-results

mv target/allure-results/* "Automation Reports"/Allure/target/allure-results

rm -rf target

allure generate "Automation Reports"/Allure/target/allure-results -o "Automation Reports"/Allure/allure-report -v 1.4.0

cd "Automation Reports"/Allure

allure report open
