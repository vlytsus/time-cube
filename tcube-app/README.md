## Java Application

Application to connect and control Time Cube from PC

For serial communication with Arduino connected via USB on Windows x64 you have to use rxtxSerial.dll compiled for Windows x64.
I found it on http://fizzed.com/oss/rxtx-for-java.
However it works only with attached RXTXcomm.jar (which isn't exist in official Maven repo)

So you have to download rxtxSerial.dll and RXTXcomm.jar and register jar in locval maven repo.
Files are also attached here in the git project.

To install jar you have to call:
```bash
mvn install:install-file -Dfile=RXTXcomm.jar -DgroupId=mfizz.com -DartifactId=mfz-rxtx -Dversion=2.2.0 -Dpackaging=jar
```
### How to build executable jar
To compile sources and all dependencies plese call:
```
mvn clean package
```
Then copy following files to desired place:
* run.bat
* rxtxSerial.dll
* log_analytics.xlsx
* /target/tcube-1.0.0-SNAPSHOT.jar 
* /target/lib

Like that:

![Destination folder](folder.jpg?raw=true "Destination folder")
