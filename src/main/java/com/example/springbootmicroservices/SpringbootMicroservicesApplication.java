package com.example.springbootmicroservices;

import com.example.springbootmicroservices.chromedriver.DriverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringbootMicroservicesApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootMicroservicesApplication.class);


    public static void main(String[] args) {
        DriverUtil.init();
        SpringApplication.run(SpringbootMicroservicesApplication.class, args);
    }




}
