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
public class TarefaGetDto {

	private Long id;
	
	@NotEmpty(message = "Você deve informar o cpf do solicitante.")
	private String cpfProdutor;
	
	private List<DetailsServiceResportDto> atendimentos;
}
