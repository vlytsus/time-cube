# time-cube
Time cube to track working time. Arduino based and USB connected cube sends state via USB to Java application. It stores time markers when you flip cube. Each cube side is assigned with some activity like tasks, entertainment, coffee breaks. So, at the end of the day you can see activities log.

For serial communication with Arduino connected via USB on Windows x64 you have to use rxtxSerial.dll compiled for Windows x64.
I found it on http://fizzed.com/oss/rxtx-for-java.
However it works only with attached RXTXcomm.jar (which isn't exist in official Maven repo)

So you have to download rxtxSerial.dll and RXTXcomm.jar and register jar in locval maven repo.
Files are also attached here in the git project.

To install jar you have to call:
```bash
mvn install:install-file -Dfile=RXTXcomm.jar -DgroupId=mfizz.com -DartifactId=mfz-rxtx -Dversion=2.2.0 -Dpackaging=jar
```
