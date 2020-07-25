package com.jp.eslocapi.api.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TarefaPostDto {

	private Long id;
	
	@NotEmpty(message = "Você deve informar o cpf do solicitante.")
	private List<String> cpfProdutor;
	
	private String observacoes;
	
	@NotEmpty(message = "Você deve informar pelo menos um serviço.")
	private List<DetailServiceDto> tipoServico;
}
