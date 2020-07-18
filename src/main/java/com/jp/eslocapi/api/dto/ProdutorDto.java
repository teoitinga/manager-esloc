package com.jp.eslocapi.api.dto;

import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty
	private String nome;
	
	@NotEmpty
	private String cpf;
	
	@NotEmpty
	private String fone;

}
