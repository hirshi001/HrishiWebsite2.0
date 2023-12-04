package com.hirshi001.springbootmicroservices;

import com.hirshi001.springbootmicroservices.chromedriver.DriverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class SpringbootMicroservicesApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootMicroservicesApplication.class);


    public static void main(String[] args) {
        DriverUtil.init();
        SpringApplication.run(SpringbootMicroservicesApplication.class, args);
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
        LOG.info("Context closed event received");
        DriverUtil.destroy();
        LOG.info("Chrome driver destroyed");
    }




}
