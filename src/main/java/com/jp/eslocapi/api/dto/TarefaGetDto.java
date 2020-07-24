package com.jp.eslocapi.api.dto;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	private String cpfProdutor;
	
	private String nomeDoProdutor;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataSolicitacao;
	
	private List<DetailsServiceResportDto> atendimentos;
}
