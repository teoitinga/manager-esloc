package com.jp.eslocapi.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorDto {

	private Long id;
	
	@NotEmpty(message = "Você deve informar o nome.")
	private String nome;
	
	@NotEmpty(message = "Você deve informar o cpf.")
	private String cpf;
	
	@NotEmpty(message = "Deve possuir um telefone de contato.")
	private String fone;
	
	@NotNull(message = "Deve informar a data de nascimento.")
	@NotEmpty(message = "Deve informar a data de nascimento.")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataNascimento;

}
