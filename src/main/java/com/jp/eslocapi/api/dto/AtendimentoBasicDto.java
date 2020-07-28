package com.jp.eslocapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentoBasicDto {
	private String id;
	private String cadastro;
	private String descricao;
	private String produtor;
	private String servico;
	private String observacao;

}
