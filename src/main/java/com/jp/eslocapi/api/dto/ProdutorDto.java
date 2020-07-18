package com.jp.eslocapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProdutorDto {
	private Long id;
	private String nome;
	private String cpf;
	private String fone;

}
