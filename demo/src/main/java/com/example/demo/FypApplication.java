package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.pdftron.pdf.*;

@SpringBootApplication
public class FypApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FypApplication.class, args);

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FypApplication.class);
	}

}
