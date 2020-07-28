package com.jp.eslocapi.api.resources;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.AtendimentoBasicDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.exceptions.ApiErrors;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.exceptions.ServiceNotFound;
import com.jp.eslocapi.api.services.AtendimentoService;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.Gerenciador;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/tarefas")
@Slf4j
public class TarefaController {
	
	@Autowired
	private Gerenciador gerenciador;
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create( @RequestBody @Valid TarefaPostDto dto) {
		log.info("Recebindo: {}",dto.getProdutorInfo());
		this.gerenciador.buildTarefa(dto);
		
	}
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<AtendimentoBasicDto> obtemAtendimentos() {
		return this.atendimentoService.findAll();
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
	@ExceptionHandler(ServiceNotFound.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleServiceNotFound(ServiceNotFound ex) {
		
		return new ApiErrors(ex.getMessage());
	}
}
