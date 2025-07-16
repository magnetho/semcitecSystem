package com.luanpereira.semcitecsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SemcitecsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemcitecsystemApplication.class, args);
	}

}
