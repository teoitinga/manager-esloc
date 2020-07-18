package com.jp.eslocapi.api.entities;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Produtor {
	private Long id;
	private String nome;
	private String cpf;
	private String fone;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataAtualizacao;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
