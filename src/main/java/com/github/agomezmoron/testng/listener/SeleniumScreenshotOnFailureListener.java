/**
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Alejandro Gómez Morón
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.agomezmoron.testng.listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

/**
 * It takes a screenshot through the Selenium WebDriver.
 * Based on https://github.com/yev/seleniumMvnScreenshot
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
public class SeleniumScreenshotOnFailureListener extends ScreenshotOnFailureListenerStrategy {

    /**
     * Log instance.
     */
    private final static Logger LOGGER = Logger.getLogger(SeleniumScreenshotOnFailureListener.class);

    /**
     * @see {@link ScreenshotOnFailureListenerStrategy#takeScreenshot(ITestResult)}.
     */
    protected void takeScreenshot(ITestResult tr, Path path) {
        WebDriver webDriver = getWebDriverByReflection(tr.getInstance());
        if (webDriver == null) {
            System.err.println(String
                    .format("The test class '%s' does not have any field/method of type 'org.openqa.selenium.WebDriver'. "
                            + "ScreenshotTestListener can not continue.", tr.getInstance().getClass().getName()));
            return;
        }

        File f = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(f.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOGGER.error("An error occurred during the screenshot copy file operation:", ex);
        }
    }

    /**
     * Getting the all the inherited fields.
     * @param type of the class from where we want to retrieve the {@link Field} list.
     * @return a {@link List} of {@link Field}.
     */
    private static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * Getting the all the inherited methods.
     * @param type of the class from where we want to retrieve the {@link Method} list.
     * @return a {@link List} of {@link Method}.
     */
    private static List<Method> getInheritedMethod(Class<?> type) {
        List<Method> fields = new ArrayList<Method>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return fields;
    }

    /**
     * It tries to get the driver that is active in execution time.
     * @param obj to be use in the Reflection API.
     * @return a {@link WebDriver} instance.
     */
    private WebDriver getWebDriverByReflection(Object obj) {
        Class<?> c = obj.getClass();
        // Field[] fields = c.getDeclaredFields();
        List<Field> fields = getInheritedFields(obj.getClass());
        WebDriver webDriver = null;
        for (Field eachField : fields) {
            eachField.setAccessible(true);
            if (WebDriver.class.isAssignableFrom(eachField.getType())) {
                try {
                    webDriver = (WebDriver) eachField.get(obj);
                } catch (IllegalAccessException ex) {
                    LOGGER.error(String.format("error accessing [%s] property of the [%s] instance", eachField
                            .getName(), obj.getClass().getName()), ex);
                    return null;
                } finally {
                    eachField.setAccessible(false);
                }
                break;
            }
        }
        if (webDriver == null) {
            webDriver = tryToFindWebDriverInPublic(obj);
        }
        if (webDriver == null) {
            webDriver = tryToFindWebDriverInPrivate(obj);
        }
        return webDriver;
    }

    /**
     * Try to find out the WebDriver within public methods, including the inherited methods
     * @param obj to be used in the Reflection API.
     * @return a {@link WebDriver} instance.
     */
    private static WebDriver tryToFindWebDriverInPublic(Object obj) {
        List<Method> methods = getInheritedMethod(obj.getClass());
        return processMethods(methods, obj);
    }

    /**
     * Try to find out the WebDriver within private methods, including the inherited methods
     * @param obj to be used in the Reflection API.
     * @return a {@link WebDriver} instance.
     */
    private static WebDriver tryToFindWebDriverInPrivate(Object obj) {
        List<Method> methods = getInheritedMethod(obj.getClass());
        return processMethods(methods, obj);
    }

    /**
     * It process the methods to retrieve the {@link WebDriver } instance.
     * @param methods to be analyzed.
     * @param obj to be used.
     * @return a {@link WebDriver} instance.
     */
    private static WebDriver processMethods(List<Method> methods, Object obj) {
        for (Method eachMethod : methods) {
            if (WebDriver.class.isAssignableFrom(eachMethod.getReturnType())) {
                eachMethod.setAccessible(true);
                try {
                    return (WebDriver) eachMethod.invoke(obj);
                } catch (Exception ex) {
                    LOGGER.error(String.format("error accessing [%s] method of the [%s] instance.", eachMethod
                            .getName(), obj.getClass().getName()), ex);
                } finally {
                    eachMethod.setAccessible(false);
                }
            }
        }
        return null;
    }

    /**
     * @see {@link ScreenshotOnFailureListenerStrategy#getScreenshotStrategyName()}.
     */
    @Override
    protected String getScreenshotStrategyName() {
        return "Selenium";
    }

}
