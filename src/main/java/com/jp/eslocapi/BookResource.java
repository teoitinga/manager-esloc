package com.jp.eslocapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.entities.Book;
import com.jp.eslocapi.dto.BookDto;
import com.jp.eslocapi.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookResource {
	
	private BookService service;
	
	public BookResource(BookService service) {
		this.service = service;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDto create(@RequestBody BookDto dto) {
		Book book = service.save(Book.builder()
				.author(dto.getAuthor())
				.isbn(dto.getIsbn())
				.title(dto.getTitle()).build()
				);
		return BookDto.builder()
				.id(book.getId())
				.author(book.getAuthor())
				.title(book.getTitle())
				.isbn(book.getIsbn())
				.build();
	}
}
