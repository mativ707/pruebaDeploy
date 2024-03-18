package com.mativ707.Peluqueria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class PeluqueriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaApplication.class, args);
	}

}
