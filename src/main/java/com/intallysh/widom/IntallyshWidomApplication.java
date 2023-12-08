package com.intallysh.widom;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntallyshWidomApplication {
	
private static final Logger logger = org.slf4j.LoggerFactory.getLogger(IntallyshWidomApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(IntallyshWidomApplication.class, args);
		logger.warn("Open This Url for API Definitions  :  "+"http://localhost:9190/swagger-ui/index.html");
	}

}
