# Time Cube
Finally I’ve made something useful from my Arduino. The Time Cube is a gadget to track your time. Flip it to Work->Learn->Chores->Rest and it will count the time you spend on that activity. I hope it will motivate me to “learn” more and collect statistic about my workday. I plan to add new features like notifications about rest, emails, and calendar appointments.

This project consist of Arduino and Java application which can run on desktop PC. It doesn't require any 3rd party cloud services or internet connection.

Arduino part just collects XYZ positions of the cube nad sends it so serial port once per second. Client is Java-based desktop application written using JavaFX for UI part. It contains whole business logic and is fully customizable to parse messages from serial port (USB) and track the statistic.

You can find detailed instructions about how to build Time Cube in my Instructables project: https://www.instructables.com/id/Time-Cube-Arduino-Time-Tracking-Gadget/

![Time Cube](time_cube.jpg?raw=true "Time Cube in action")

Application writes comma-separated events log to time-log.csv file. It can be used by log_analytics.xlsx to present data as graphical reports in MS Excel.

![log_analytics.xlsx](excell_analytics.jpg?raw=true "log_analytics.xlsx")

## Bill of materials
* Arduino micro like that: https://store.arduino.cc/arduino-micro
* Accelerometer ADXL345: https://www.analog.com/en/products/adxl345.html
* Some carton / plastic cube box
* USB cable

## Useful materials
* [How To Track Orientation with Arduino and ADXL345 Accelerometer](https://howtomechatronics.com/tutorials/arduino/how-to-track-orientation-with-arduino-and-adxl345-accelerometer)
* [Java serial connection to Arduino](https://playground.arduino.cc/Interfacing/Java/)

## Future plans
* Blink LED in "Rest" mode to keep attention and not forget to flip it back to work when you returns from the coffee break
* Blink LED if htere is new email/calendar event.
* Blink LED after 1h of work to go for a short walk or drink a cup of water and to not sit still.
* Make wireless version of Time Cube (Bluetooth or WiFi)
* Add ability to manually correct statistics.
* Add day graph with statistics
* TODO
