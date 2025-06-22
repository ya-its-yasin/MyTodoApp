Option Explicit

Dim shell
Set shell = CreateObject("WScript.Shell")

' Run app using specific JDK, logs to file
shell.Run "cmd /c javaw -jar MytodoApp-0.0.1-SNAPSHOT.jar --spring.config.location=file:./myconfig/application.properties > myconfig\myapp-log.log 2>&1", 0, False

' Show popup
shell.Popup "Starting MyApp... Please wait..", 15, "Loading", 64 + 4096

' Open browser
shell.Run "chrome http://localhost:8081", 1, False