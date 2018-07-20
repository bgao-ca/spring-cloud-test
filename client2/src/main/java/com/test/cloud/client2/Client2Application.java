package com.test.cloud.client2;

import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableEurekaClient
@RestController
@EnableHystrix
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
public class Client2Application {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;
    @Autowired
    Environment environment;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/greeting")
    public String greeting() {
        String port = environment.getProperty("local.server.port");
        String message = String.format("Hello from '%s:%s'!", eurekaClient.getApplication(appName).getName(), port);
        log.debug(message);
        return message;
    }

    public static void main(String[] args) {
        SpringApplication.run(Client2Application.class, args);
    }
}
