package com.jp.eslocapi;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
	
	@Value("${esloc.date.folder}")
	private String DATA_FORMAT_FOLDER;// = "yyyy-MM-dd";

	@Value("${esloc.date.view}")
	private String DATA_FORMAT_VIEW;// = dd/MM/yyyy;

	@Value("${esloc.date.key}")
	private String DATA_FORMAT_KEY;// = yyyyMMddhhmm;
	
	@Bean
	public DateTimeFormatter folderDateTimeFormater() {
		return DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER);
	}
	
	@Bean
	public DateTimeFormatter viewDateTimeFormater() {
		return DateTimeFormatter.ofPattern(DATA_FORMAT_VIEW);
	}
	@Bean
	public DateTimeFormatter keyDateTimeFormater() {
		return DateTimeFormatter.ofPattern(DATA_FORMAT_KEY);
	}
}
