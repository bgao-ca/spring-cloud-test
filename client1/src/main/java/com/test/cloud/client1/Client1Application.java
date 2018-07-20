package com.test.cloud.client1;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RoundRobinRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.cloud.netflix.turbine.EnableTurbine;

@Slf4j
@RestController

//@EnableHystrix
//@EnableTurbine
@RibbonClients(
        defaultConfiguration = {EurekaRibbonClientConfiguration.class, Client2ClientConfiguration.class }
)
//@RibbonClient(name = "em-client2", configuration = Client2ClientConfiguration.class)
@EnableDiscoveryClient
@SpringBootApplication
public class Client1Application {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private Environment environment;

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/greeting")
    public String greeting() {
        String port = environment.getProperty("local.server.port");
        String msgFromClient2 = restTemplate.getForObject("http://client2/greeting", String.class);
        String message = String.format("Hello from '%s:%s' from [%s]!", appName, port, msgFromClient2);
        log.debug(message);
        return message;
    }

    public static void main(String[] args) {
        SpringApplication.run(Client1Application.class, args);
    }
}

@Configuration
class Client2ClientConfiguration {

    @Bean
    public IPing ribbonPing() {
        PingUrl pingUrl = new PingUrl();
        pingUrl.setPingAppendString("/actuator/info");
        return pingUrl;
    }

    @Bean
    public IRule ribbonRule() {
//        return new AvailabilityFilteringRule();
        return  new RoundRobinRule();
    }
}
