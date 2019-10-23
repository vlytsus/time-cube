# Time Cube
Finally I’ve made something useful from my Arduino. The Time Cube is a gadget to track your time. Flip it to Work->Learn->Chores->Rest and it will count the time you spend on that activity. I hope it will motivate me to “learn” more and collect statistic about my workday. I plan to add new features like notifications about rest, emails, and calendar appointments. 

Arduino part just collects XYZ positions of the cube nad sends it so serial port each second. Client application is written in Java using JavaFX for UI part. It parses messages from serial port (USB) and tracks the statistic. Application will create time-log.csv file whit all notifications, so you can analyse data in MS Excel.

![Time Cube](time_cube.jpg?raw=true "Time Cube in action")
![adxl345-accelerometer](https://howtomechatronics.com/wp-content/uploads/2019/03/Arduino-and-ADXL345-Accelerometer-Circuit-Diagram-768x426.png?raw=true "adxl345-accelerometer")

## Bill of materials
* Arduino micro like that: https://store.arduino.cc/arduino-micro
* Accelerometer ADXL345: https://www.analog.com/en/products/adxl345.html
* Some carton / plastic cube box
* USB cable

## Useful materials
[How To Track Orientation with Arduino and ADXL345 Accelerometer](https://howtomechatronics.com/tutorials/arduino/how-to-track-orientation-with-arduino-and-adxl345-accelerometer)

## Future plans
* Blink LED in "Rest" mode to keep attention and not forget to flip it back to work when you returns from the coffee break
* Blink LED if htere is new email/calendar event.
* Blink LED after 1h of work to go for a short walk or drink a cup of water and to not sit still.
* Make wireless version of Time Cube (Bluetooth or WiFi)
* Add ability to manually correct statistics.
* Add day graph with statistics
* TODO
