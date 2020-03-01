package com.challenge.twitterconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TwitterConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterConsumerApplication.class, args);
	}

}
