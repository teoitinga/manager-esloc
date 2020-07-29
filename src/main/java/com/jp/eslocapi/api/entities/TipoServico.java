package com.jp.eslocapi.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

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
public class TipoServico {
	//@Column
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//private Long id;

	@Id
	@Column
	private String tipo;
	
	@Column
	private String descricaoTipo;
	
	@Column
	private int tempoEstimado;
	
	@Column
	private BigDecimal valorReferencia;
	
	@Column
	private LocalDate dataAtualizacao;
	
	@Column
	private LocalDate dataCadastro;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDate.now();

	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDate.now();
	}	
	
}
