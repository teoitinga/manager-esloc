package com.jp.eslocapi.api.resources;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.EnumPermissao;
import com.jp.eslocapi.api.entities.EnumType;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.exceptions.ApiErrors;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@RestController
@RequestMapping("api/v1/produtores")
public class ProdutorController {
	
	private ProdutorService service;
	
	public ProdutorController(ProdutorService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutorDto create( @RequestBody @Valid ProdutorDto dto) {
		
		Persona toSaved = service.toProdutor(dto);
		toSaved = service.save(toSaved);
		
		ProdutorDto response = service.toProdutorDto(toSaved);
		
		return response;
	}
	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutorDto getProdutor(@PathVariable Long id) {
		
		Persona toSaved = service.getById(id);
		
		toSaved.setTipo(EnumType.AGRICULTOR_FAMILIAR);
		toSaved.setPermissao(EnumPermissao.AGRICULTOR_FAMILIAR);
		
		ProdutorDto response = service.toProdutorDto(toSaved);
		
		return response;
	}
	@GetMapping("cpf/{cpf}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ProdutorDto getFindByCpf(@PathVariable String cpf) {
		
		Persona toSaved = service.whatIsCpf(cpf);
		
		ProdutorDto response = service.toProdutorDto(toSaved);
		
		return response;
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProdutor(@PathVariable Long id) {
		
		Persona toDeleted= service.getById(id);
		service.delete(toDeleted);

	}
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutorDto updadeProdutor(@PathVariable Long id, @RequestBody @Valid ProdutorDto dto) {
		
		ProdutorDto response;
		
		Persona toUpdated = service.getById(id);
		
		dto.setId(toUpdated.getId());
		
		response = service.update(dto);
		
		return response;	
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsException(MethodArgumentNotValidException ex) {

		BindingResult resultErrors = ex.getBindingResult();
		
		return new ApiErrors(resultErrors);
	}
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrors handleBusinessException(BusinessException ex) {
		
		return new ApiErrors(ex);
	}
	@ExceptionHandler(ProdutorNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrors handleProdutorNotFound(ProdutorNotFound ex) {
		
		return new ApiErrors(ex);
	}
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleProdutorNotFound(ConstraintViolationException ex) {
		
		return new ApiErrors("CPF informado não é válido!");
	}

}
