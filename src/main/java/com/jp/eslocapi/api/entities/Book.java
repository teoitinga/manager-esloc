package com.jp.eslocapi.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

	private Long id;
	@NotEmpty
	private String author;
	@NotEmpty
	private String title;
	@NotEmpty
	private String isbn;
}
