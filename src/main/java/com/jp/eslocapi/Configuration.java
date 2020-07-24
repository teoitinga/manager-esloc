package com.jp.eslocapi;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Configuration {

	@Bean
	public DateTimeFormatter dateTimeFormater() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
}
