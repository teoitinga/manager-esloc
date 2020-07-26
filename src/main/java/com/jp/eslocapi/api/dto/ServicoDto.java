package com.jp.eslocapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicoDto {

	private String legenda;
	
	private String descricao;
	
	private String tempoNecessario;
	
	private String valorReferencia;
}
