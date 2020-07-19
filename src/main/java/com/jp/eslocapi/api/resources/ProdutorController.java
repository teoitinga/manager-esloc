package com.jp.eslocapi.api.resources;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.exceptions.ApiErrors;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@RestController
@RequestMapping("/api/v1/produtores")
public class ProdutorController {
	
	private ProdutorService service;
	
	public ProdutorController(ProdutorService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutorDto create(@Valid @RequestBody ProdutorDto dto) {
		
		Produtor toSaved = Produtor.builder()
				.nome(dto.getNome())
				.cpf(dto.getCpf())
				.fone(dto.getFone())
				.build();
		toSaved = service.save(toSaved);
		
		ProdutorDto response = ProdutorDto.builder()
				.id(toSaved.getId())
				.nome(toSaved.getNome())
				.cpf(toSaved.getCpf())
				.fone(toSaved.getFone())
				.build();
		return response;
	}
	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutorDto getProdutor(@PathVariable Long id) {
		
		Produtor toSaved = service.getById(id);
		
		ProdutorDto response = ProdutorDto.builder()
				.id(toSaved.getId())
				.nome(toSaved.getNome())
				.cpf(toSaved.getCpf())
				.fone(toSaved.getFone())
				.build();
		
		return response;
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsException(MethodArgumentNotValidException ex) {

		BindingResult resultErrors = ex.getBindingResult();
		
		return new ApiErrors(resultErrors);
	}
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBusinessException(BusinessException ex) {
		
		return new ApiErrors(ex);
	}
	@ExceptionHandler(ProdutorNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrors handleBusinessException(ProdutorNotFound ex) {
		
		return new ApiErrors(ex);
	}

}
