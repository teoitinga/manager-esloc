package com.jp.eslocapi.api.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.TarefaGetDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Tarefa;
import com.jp.eslocapi.services.TarefaService;

@RestController
@RequestMapping("api/v1/tarefas")
public class TarefaController {
	@Autowired
	TarefaService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TarefaGetDto create( @RequestBody @Valid TarefaPostDto dto) {
		
		Tarefa tarefa = this.service.managerDto(dto);
		
		return this.service.toTarefaGetDto(tarefa);
		
	}
}
