package com.jp.eslocapi.api.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.services.Gerenciador;

@RestController
@RequestMapping("api/v1/tarefas")
public class TarefaController {
	
	@Autowired
	private Gerenciador gerenciador;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create( @RequestBody @Valid TarefaPostDto dto) {

		this.gerenciador.buildTarefa(dto);
		
	}
}
