package com.jp.eslocapi.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Atendimento {
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
	private String observacoes;

	@Column
	private String tarefaDescricao;
	
	@Column
	private LocalDateTime dataCadastro;
	
	@Column
	private LocalDateTime dataAtualizacao;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service")
	private TipoServico tiposervico;

	@Column
	private BigDecimal valorDoServico;
	
	@Column
	private Boolean emitiuDAE;
	
	@Column
	private LocalDate datapgtoDAE;

	@Column
	private Boolean emitiuART;

	@Column
	private LocalDate datapgtoART;	
	
	@Column
	private LocalDateTime dataFinalizada;
	
	@Column
	@Enumerated(EnumType.STRING)
	private EnumStatus statusTarefa;
	
	@Column
	private LocalDate dataConclusaoPrevista;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
