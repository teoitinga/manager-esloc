package com.jp.eslocapi.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

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
public class DetalheServico {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service")
	private TipoServico tiposervico;
	
	private BigDecimal valorDoServico;
	
	private Boolean emitiuDAE;
	
	private LocalDate datapgtoDAE;

	private Boolean emitiuART;

	private LocalDate datapgtoART;	
	
	private String tarefaDescricao;
	
	private LocalDateTime dataCadastro;
	
	private LocalDateTime dataAtualizacao;
	
	private LocalDateTime dataFinalizada;

	private EnumStatus statusTarefa;	

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String dataConclusaoPrevista;
	
	@PrePersist
	public void setDataCadastro() {
		this.dataCadastro = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setDataUptade() {
		this.dataAtualizacao = LocalDateTime.now();
	}
}
