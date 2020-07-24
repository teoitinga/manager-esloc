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
public class DetailsServiceResportDto {
	
	private String valorDoServico;
	
	private String emitiuDAE;
	
	private String emitiuART;
	
	private String tarefaDescricao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataConclusaoPrevista;
}
