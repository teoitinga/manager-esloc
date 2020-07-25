package com.jp.eslocapi.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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
public class Tarefa {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@NotNull(message = "Você deve informar o produtor que solicitou o serviço.")
	private Persona produtor;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Persona responsavel;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Persona emissor;
	
	@Column
	private Boolean emitiuDAE;
	
	@Column
	private String observacoes;
	
	@Column
	private LocalDateTime dataCadastro;
	
	@Column
	private LocalDateTime dataAtualizacao;

	@OneToMany
	private List<DetalheServico> detalhes;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
