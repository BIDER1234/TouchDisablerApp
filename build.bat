@echo off
cd /d %~dp0
cd app
..\gradlew assembleDebug
pause
