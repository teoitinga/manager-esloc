package com.jp.eslocapi.api.dto;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

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
	private String cpfProdutor;
	
	private String valorDoServico;
	
	private String emitiuDAE;
	
	private String emitiuART;
	
	private String tarefaDescricao;
	
	private String observacoes;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataConclusaoPrevista;
	
	private String tipoServico;
}
