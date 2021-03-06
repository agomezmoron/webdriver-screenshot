# WebDriver screenshot

<img src="/resources/images/selenium-logo.png" height="128" />
<img src="/resources/images/testng-logo.png" height="128" />
<img src="/resources/images/appium-logo.jpg" height="128" />
<img src="/resources/images/screenshot.png" height="128" />

Description
-----------

It allows your Selenium & Appium projects to store automatically screenshots if a test fails integrating with TestNG. This project is inspired on [yev's idea](https://github.com/yev).

This project allows you to take screenshot from two different sources.

 1. Using the [Selenium WebDriver screenshot utility](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/TakesScreenshot.html)
 2. Using the [Appium WebDriver screenshot utility](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/TakesScreenshot.html)
 3. Using the native system screenshot.


Getting started
-----------

###How to use

--
 1. Add the repository:

   ```xml
  <repositories>
		<repository>
			<id>webdricer-screenshot</id>
			<name>WebDriver screenshot library built by agomezmoron</name>
			<url>https://raw.github.com/agomezmoron/webdriver-screenshot/mvn-repo</url>
		</repository>
	</repositories>
    ```
 2. Adding the following maven dependency in you ```pom.xml``` file:


    ```xml 
    <dependency>
      <groupId>com.github.agomezmoron</groupId>
      <artifactId>webdriver-screenshot</artifactId>
      <version>1.0.3</version>
    </dependency>
    ```
    
 3. <a name="annotation"></a> Adding next annotation:
 
 For Selenium projects:

 ```@org.testng.annotations.Listeners({com.github.agomezmoron.testng.listener.SeleniumScreenshotOnFailureListener.class, com.github.agomezmoron.testng.listener.SystemScreenshotOnFailureListener.class})``` to your TestNG Selenium class:
   ```java
   import org.testng.annotations.Listeners;
   import com.github.agomezmoron.testng.listener.SeleniumScreenshotOnFailureListener;
   import com.github.agomezmoron.testng.listener.SystemScreenshotOnFailureListener;
   
   @Listeners({SeleniumScreenshotOnFailureListener.class, SystemScreenshotOnFailureListener.class})
   public class SeleniumTestClass {
    .....
   }
    ```
  For Appium projects:

 ```@org.testng.annotations.Listeners({com.github.agomezmoron.testng.listener.AppiumScreenshotOnFailureListener.class, com.github.agomezmoron.testng.listener.SystemScreenshotOnFailureListener.class})``` to your TestNG Selenium class:
   ```java
   import org.testng.annotations.Listeners;
   import com.github.agomezmoron.testng.listener.AppiumScreenshotOnFailureListener;
   import com.github.agomezmoron.testng.listener.SystemScreenshotOnFailureListener;
   
   @Listeners({AppiumScreenshotOnFailureListener.class, SystemScreenshotOnFailureListener.class})
   public class AppiumTestClass {
    .....
   }
    ```
 
 4. Subsequently the plugin will find by reflection the **WebDriver instance** you are using and will do the rest for you.
  
### How will I get the output?

After running your Selenium & Appium tests through TestNG, you will find a screenshot in the target folder for each failed test.

The filename pattern is: screenshot_typeOfScreenShot_testMethodName_timestamp.png

Ex: 
 * screenshot_Selenium_myTest_1467812008.png
 * screenshot_Appium_myTest_1467812008.png
 * screenshot_System_myTest_1467812008.png
