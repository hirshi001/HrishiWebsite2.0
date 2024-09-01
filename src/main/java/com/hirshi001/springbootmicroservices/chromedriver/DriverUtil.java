package com.hirshi001.springbootmicroservices.chromedriver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@Slf4j
public class DriverUtil {

    private static final DriverUtil INSTANCE = new DriverUtil();


    public static boolean init() throws Exception {
        try {
            INSTANCE.initChromeDriver();
            return true;
        } catch (Exception e) {
            INSTANCE.driver = null;
            log.warn("Error initializing Chrome Driver", e);
            log.warn("Path may have changed. If on Mac, try running \"brew info chromedriver\" to find the path");
            throw e;
        }
    }

    public static void destroy() {
        ChromeDriver driver = INSTANCE.driver;

        if (driver != null)
            driver.quit();
    }

    public static boolean driverExists() {
        return INSTANCE.driver != null;
    }

    public static WebDriver getDriver() throws DriverNotFoundException {
        if (INSTANCE.driver == null)
            throw new DriverNotFoundException();
        return INSTANCE.driver;
    }


    private ChromeDriver driver;
    @Value("${chrome-driver.path}")
    private String chrome_driver_path;
    @Value("${chrome-driver.use-path:#{false}}")
    private boolean chrome_driver_use_path;


    private DriverUtil() {

    }

    private void initChromeDriver() throws Exception {
        log.info("Initializing Chrome Driver");
        if (chrome_driver_use_path) {
            System.setProperty("webdriver.chrome.driver", chrome_driver_path);
        }
        log.info("Chrome driver path: " + System.getProperty("webdriver.chrome.driver"));

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--force-dark-mode");

        options.setImplicitWaitTimeout(Duration.ofSeconds(10));
        options.setScriptTimeout(Duration.ofSeconds(10));
        options.setPageLoadTimeout(Duration.ofSeconds(10));

        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            log.info("Disabling dev shm usage for Chrome Driver");
            options.addArguments("--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        log.info("Chrome Driver created.");
    }
}
