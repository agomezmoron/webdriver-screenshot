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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Package class.
 * 
 * Strategy to take an screenshot after a test failure.
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
abstract class ScreenshotOnFailureListenerStrategy extends TestListenerAdapter {

    /**
     * Log instance.
     */
    private final static Logger LOGGER = Logger.getLogger(ScreenshotOnFailureListenerStrategy.class);

    /**
     * Folder where the project is.
     */
    private static String FOLDER = ".";

    static {
        try {
            // Getting the project path
            FOLDER = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            LOGGER.error("Error while detecting current working dir.", ex);
        }
    }

    /**
     * @see TestListenerAdapter#onTestFailure(ITestResult).
     */
    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);

        final long timestamp = new Date().getTime();
        Path screenshotPath = Paths.get(FOLDER, "target", "screenshot_" + getScreenshotStrategyName() + "_"
                + tr.getMethod().getMethodName() + "_" + timestamp + ".png");

        this.takeScreenshot(tr, screenshotPath);

    }

    /**
     * It takes an screenshot from a failed test.
     * @param tr {@link ITestResult} instance.
     * @param path {@link Path} to be saved the screenshot.
     * @return {@link File} instance with the screenshot.
     */
    protected abstract void takeScreenshot(ITestResult tr, Path path);

    /**
     * It returns the strategy name.
     * @return {@link String} with the strategy name.
     */
    protected abstract String getScreenshotStrategyName();

}
