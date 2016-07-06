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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.testng.ITestResult;

/**
 * It takes a screenshot through the system. It takes the screenshot like an Impr pant function.
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
public class SystemScreenshotOnFailureListener extends ScreenshotOnFailureListenerStrategy {

    /**
     * Log instance.
     */
    private final static Logger LOGGER = Logger.getLogger(SystemScreenshotOnFailureListener.class);

    /**
     * @see {@link ScreenshotOnFailureListenerStrategy#takeScreenshot(ITestResult)}.
     */
    protected void takeScreenshot(ITestResult tr, Path path) {
        // taking system screenshot
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            ImageIO.write(image, "png", path.toFile());
        } catch (Exception ex) {
            LOGGER.error("Couldn't take the screenshot using the Java Robot", ex);
        }
    }

    /**
     * @see {@link ScreenshotOnFailureListenerStrategy#getScreenshotStrategyName()}.
     */
    @Override
    protected String getScreenshotStrategyName() {
        return "System";
    }

}
