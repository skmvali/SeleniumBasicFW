Tools used - 

 - Webdriver
 - Java
 - TestNG
 - ANT
 - ReportNG
 - Log4J


Test Design 
 - Uses Page Object design for easy readable tests and maintanance
 - Modular framework approach for better reusability of code
 - Seperated Locator strategy from User actions 
 - Flexible definition of test suites by Classes, Groups etc
  

Exception handling
 - Webdriver API wrapped with custom 'Safe' methods for handling synchronization issues, Alerts
 

Report
 
 - Makes use of ReportNG abilities to generate reports
 - Generates logs with Log4j
 - Video recording of each test(Pass/Fail)
 - Screenshot and detailed description for failed tests


Configurable
 - Flexible and configurable global test parameters and settings


Data driven - 
 - Have some libraries to support(Excel based)


Integration
 - with Jenkins CI using ANT 



Execution 
 - System tray popup that displays the current executing test name
 - Highlights each UI element before performing any interactions
 - Can execute the tests on local machine, Saucelabs, TestingBot and Grid 
 - Supports Chrome, IE, FF browsers
 - Repeats the execution of failed test to confirm the failure


Demo script -
 - Automates gmail inbox and send email functionality
 - Currently written for testing framework purposes and hence would run on FF browser and might fail few tests on IE or 
   Chrome browsers. 