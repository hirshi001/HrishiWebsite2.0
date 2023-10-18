package com.example.springbootmicroservices;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SpringbootMicroservicesApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootMicroservicesApplication.class);
    public static volatile WebDriver driver;

    @Value("${chrome-driver.path}")
    public String chrome_driver_path;


    @Value("${chrome-driver.use-path:#{false}}")
    public boolean chrome_driver_use_path;

    public static void main(String[] args) {


        SpringApplication.run(SpringbootMicroservicesApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        LOG.info("Application started...");
        LOG.info("Creating Chrome Driver...");
        if(chrome_driver_use_path) {
            System.setProperty("webdriver.chrome.driver", chrome_driver_path);
        }
        LOG.info("Chrome driver path: " + System.getProperty("webdriver.chrome.driver"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("no-sandbox");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");

        driver = new ChromeDriver(options);
        LOG.info("Chrome Driver created.");
    }


}
