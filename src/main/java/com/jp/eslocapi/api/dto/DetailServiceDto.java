package com.jp.eslocapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailServiceDto {
	
	private String tipoServico;
	
	private String valorDoServico;
	
	private String tarefaDescricao;
	
	private String emitiuDAE;
	
	private String emitiuART;
	
	
}
