# Selenium screenshot

<img src="/resources/images/selenium-logo.png" height="128" />
<img src="/resources/images/testng-logo.png" height="128" />
<img src="/resources/images/screenshot.png" height="128" />

Description
-----------

It allows your Selenium project to store automatically screenshots if a test fails integrating with TestNG. This project is inspired on [yev's idea](https://github.com/yev).

This project allows you to take screenshot from two different sources.

 1. Using the [Selenium WebDriver screenshot utility](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/TakesScreenshot.html)
 2. Using the native system screenshot.


Getting started
-----------

###How to use

--
 1. Adding the following maven dependency in you ```pom.xml``` file:


    ```xml 
    <dependency>
      <groupId>com.github.agomezmoron</groupId>
      <artifactId>selenium-screenshot</artifactId>
      <version>1.0.0</version>
    </dependency>
    ```
    
 2. <a name="annotation"></a> Adding next annotation: ```@org.testng.annotations.Listeners({com.github.agomezmoron.testng.listener.SeleniumScreenshotOnFailureListener.class, com.github.agomezmoron.testng.listener.SystemScreenshotOnFailureListener.class})``` to your TestNG Selenium class:
   ```java
   import org.testng.annotations.Listeners;
   import com.github.agomezmoron.testng.listener;
   
   @Listeners({SeleniumScreenshotOnFailureListener.class, SystemScreenshotOnFailureListener.class})
   public class SeleniumTestClass {
    .....
   }
    ```
 
 3. Subsequently the plugin will find by reflection the **WebDriver instance** you are using and will do the rest for you.
  
### How will I get the output?

After running your Selenium tests through TestNG, you will find a screenshot in the target folder for each failed test.

The filename pattern is: screenshot_typeOfScreenShot_testMethodName_timestamp.png

Ex: 
 * screenshot_Selenium_myTest_1467812008.png
 * screenshot_System_myTest_1467812008.png
