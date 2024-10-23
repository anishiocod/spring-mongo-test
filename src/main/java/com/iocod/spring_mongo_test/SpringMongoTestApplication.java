package com.iocod.spring_mongo_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.iocod.spring_mongo_test")
public class SpringMongoTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoTestApplication.class, args);
	}

}
