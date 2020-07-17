package com.jp.eslocapi.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.exceptions.ApiErrors;
import com.jp.eslocapi.services.ProdutorService;

@RestController
@RequestMapping("/api/produtor")
public class ProdutorResource {
	
	private ProdutorService service;
	
	public ProdutorResource(ProdutorService service) {
		this.service = service;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutorDto create(@RequestBody ProdutorDto dto) {
		Produtor Savedprodutor = service.save(Produtor.builder()
				.nome(dto.getNome())
				.cpf(dto.getCpf())
				.fone(dto.getFone())
				.build()
				);
		return ProdutorDto.builder()
				.id(Savedprodutor.getId())
				.nome(Savedprodutor.getNome())
				.cpf(Savedprodutor.getCpf())
				.fone(Savedprodutor.getFone())
				.build();
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception){
		BindingResult bindingResult = exception.getBindingResult();
		return  new ApiErrors(bindingResult);
	}
}
