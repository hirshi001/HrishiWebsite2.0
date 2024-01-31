package com.hirshi001.springbootmicroservices;

import com.hirshi001.springbootmicroservices.chromedriver.DriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
@Slf4j
public class SpringbootMicroservicesApplication {



    public static void main(String[] args) {
        DriverUtil.init();
        SpringApplication.run(SpringbootMicroservicesApplication.class, args);
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
        log.info("Context closed event received");
        DriverUtil.destroy();
        log.info("Chrome driver destroyed");
    }




}
