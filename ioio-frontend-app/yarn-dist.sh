#!/bin/bash

yarn || :
yarn dist || :
mkdir target
zip target/ioio-frontend-app-${1}.zip dist/* || :
