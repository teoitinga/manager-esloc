package com.jp.eslocapi.services;

import java.util.List;

import com.jp.eslocapi.api.dto.TarefaGetDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Tarefa;

public interface TarefaService {
	Tarefa save(Tarefa tarefa);

	Tarefa getById(Long id);

//	Tarefa toTarefa(TarefaPostDto dto);
	
	Tarefa managerDto(TarefaPostDto dto);

	TarefaGetDto toTarefaGetDto(Tarefa tarefa);

	void printFile(Tarefa tarefa);

	Tarefa getById(String idTarefa);
	
	List<Tarefa> toListTarefa(TarefaPostDto dto);
}
