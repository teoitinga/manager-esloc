package com.jp.eslocapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private Long id;

	@NotEmpty
	private String author;

	@NotEmpty
	private String title;

	@NotEmpty
	private String isbn;
}
