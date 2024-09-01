package com.hirshi001.springbootmicroservices.chromedriver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;


@Slf4j
public class ScreenShotter {


    public static byte[] getScreenshot(String url) throws WebDriverException, InterruptedException {
        final WebDriver driver = DriverUtil.getDriver();

        synchronized (driver) {
            driver.get(url);
            Thread.sleep(5000);
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
    }

    public static byte[] getScreenshot(String url, int retryCount) throws DriverNotFoundException {

        for (int count = 0; count < retryCount; count++) {
            try {
                return getScreenshot(url);
            } catch (WebDriverException | InterruptedException ignored) {
            }
        }
        return null;
    }
}
