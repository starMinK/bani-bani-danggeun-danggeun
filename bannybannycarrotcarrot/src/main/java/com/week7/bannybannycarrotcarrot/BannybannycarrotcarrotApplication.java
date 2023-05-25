package com.week7.bannybannycarrotcarrot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BannybannycarrotcarrotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BannybannycarrotcarrotApplication.class, args);
	}

}
