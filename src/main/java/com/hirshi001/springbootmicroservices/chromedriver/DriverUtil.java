package com.hirshi001.springbootmicroservices.chromedriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public class DriverUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);
    private static final DriverUtil INSTANCE = new DriverUtil();


    public static void init() {
        try {
            INSTANCE.initChromeDriver();
        } catch (Exception e) {
            INSTANCE.driver = null;
            LOG.error("Error initializing Chrome Driver", e);
            LOG.error("Path may have changed. If on Mac, try running \"brew info chromedriver\" to find the path");
        }
    }
    public static void destroy() {
        WebDriver driver = getDriver();
        if(driver != null) {
            synchronized (driver) {
                driver.quit();
            }
        }
    }

    public static WebDriver getDriver() {
        return INSTANCE.driver;
    }




    private WebDriver driver;
    @Value("${chrome-driver.path}")
    private String chrome_driver_path;
    @Value("${chrome-driver.use-path:#{false}}")
    private boolean chrome_driver_use_path;


    private DriverUtil() {

    }

    private void initChromeDriver() throws Exception{
        LOG.info("Initializing Chrome Driver");
        if(chrome_driver_use_path) {
            System.setProperty("webdriver.chrome.driver", chrome_driver_path);
        }
        LOG.info("Chrome driver path: " + System.getProperty("webdriver.chrome.driver"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");

        options.setImplicitWaitTimeout(Duration.ofSeconds(10));
        options.setScriptTimeout(Duration.ofSeconds(10));
        options.setPageLoadTimeout(Duration.ofSeconds(10));

        String os = System.getProperty("os.name");
        if(os!=null && os.toLowerCase().contains("linux")) {
            LOG.info("Disabling dev shm usage for Chrome Driver");
            options.addArguments("--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        LOG.info("Chrome Driver created.");
    }
}
