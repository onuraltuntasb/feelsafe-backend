package com.feelsafe.feelsafe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FeelsafeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FeelsafeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("StartApplication...");
		runJDBC();
	}

	void runJDBC() {
		//NOTE: fill database tables in here.
	}
}
