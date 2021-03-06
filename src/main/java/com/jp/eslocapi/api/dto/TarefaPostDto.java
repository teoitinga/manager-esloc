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

	@NotEmpty(message = "Você deve informar o cpf do solicitante.")
	private List<ProdutoPostMinDto> produtorInfo;
	
	
	@NotEmpty(message = "Você deve informar pelo menos um serviço.")
	private List<AtendimentoBasicDto> tipoServico;
	
	private String dataDoAtendimento;

	private String observacoes;
}
