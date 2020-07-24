package com.jp.eslocapi.services;

import com.jp.eslocapi.api.dto.TarefaGetDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Tarefa;

public interface TarefaService {
	Tarefa save(Tarefa tarefa);

	Tarefa getById(Long id);

	Tarefa toTarefa(TarefaPostDto dto);
	
	Tarefa managerDto(TarefaPostDto dto);

	TarefaGetDto toTarefaGetDto(Tarefa tarefa);
}
