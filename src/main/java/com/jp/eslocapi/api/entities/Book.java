package com.jp.eslocapi.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
	private Long id;
	private String author;
	private String title;
	private String isbn;
}
