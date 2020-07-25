package com.jp.eslocapi.api.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Persona {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nome;
	
	@Column
	@CPF
	private String cpf;
	
	@Column
	private String fone;
	
	@Column
	private LocalDate dataNascimento;
	
	@Column
	private LocalDateTime dataCadastro;
	
	@Column
	private LocalDateTime dataAtualizacao;
	
	@Column
	private EnumType tipo;
	
	@Column
	private EnumPermissao permissao;
	
	@Column
	private String password;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
