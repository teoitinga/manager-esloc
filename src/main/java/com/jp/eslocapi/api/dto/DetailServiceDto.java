package com.jp.eslocapi.api.dto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailServiceDto {
	private Long id;
	
	private String valorDoServico;
	
	private String emitiuDAE;
	
	private String emitiuART;
	
	private String tarefaDescricao;
	
	private String tipoServico;

	private String situacao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataConclusaoPrevista;
}
