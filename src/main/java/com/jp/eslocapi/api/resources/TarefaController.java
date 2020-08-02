package com.jp.eslocapi.api.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.AtendimentosBasicGetDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.services.AtendimentoService;
import com.jp.eslocapi.services.Gerenciador;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/tarefas")
@Slf4j
@Api("Atendimentos")
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
	public List<AtendimentosBasicGetDto> obtemAtendimentos(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size
			) {
		return this.atendimentoService.findAll(PageRequest.of(page, size));
	}

}
