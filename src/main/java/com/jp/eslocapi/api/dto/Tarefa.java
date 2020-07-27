package com.jp.eslocapi.api.dto;

import java.util.List;

import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.entities.Persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

	private List<Persona> produtores;
	
	private List<Atendimento> atendimentos;
	
	private String obervacao;
}
