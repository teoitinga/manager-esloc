package com.jp.eslocapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentosBasicGetDto {
	private String id;
	private String produtor;
	private String dataDoAtendimento;
	private String CodDoServico;
	private String descricaoDoServico;
	private String emitiuDae;
	private String emitiuArt;
	private String observacao;

}