package com.jp.eslocapi.resources;

import com.jp.eslocapi.api.exceptions.ApiErrors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.jp.eslocapi.api.entities.Book;
import com.jp.eslocapi.dto.BookDto;
import com.jp.eslocapi.services.BookService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookResource {
	
	private BookService service;

	private ModelMapper modelMapper;

	public BookResource(BookService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDto create(@RequestBody @Valid BookDto dto) {

		Book entity = modelMapper.map(dto, Book.class);
		entity = service.save(entity);

		return modelMapper.map(entity, BookDto.class);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception){
		BindingResult bindingResult = exception.getBindingResult();
		return  new ApiErrors(bindingResult);
	}

}
