package com.jp.eslocapi.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.jp.eslocapi.api.dto.TipoServico;

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
	private EnumStatus statusTarefa;
	
	@Column
	private String observacoes;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataConclusaoPrevista;
	
	@Column
	private LocalDateTime dataCadastro;
	
	@Column
	private LocalDateTime dataAtualizacao;

	@OneToOne
	private TipoServico tipoServico;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
