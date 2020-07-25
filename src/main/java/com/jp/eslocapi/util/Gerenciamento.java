package com.jp.eslocapi.util;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jp.eslocapi.api.entities.DetalheServico;
import com.jp.eslocapi.api.entities.DocumentType;
import com.jp.eslocapi.api.entities.Tarefa;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Gerenciamento {
	
	@Value("${esloc.date.folder}")
	private String DATA_FORMAT_FOLDER;// = "yyyy-MM-dd";
	
	public String obtemNomeDeArquivo(Tarefa tarefa, DocumentType document) {
		StringBuilder arquivo = new StringBuilder();
		arquivo.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER)))
		.append(" ")
		.append(document.getAbreviatura())
		.append(" ")
		.append(tarefa.getProdutor()
				.getNome().toUpperCase())
		;
		
		log.info("Nome do arquivo: {}", arquivo.toString());
		
		return arquivo.toString();
	}
	public String obtemNomeDePastaDoProdutor(Tarefa tarefa) {
		StringBuilder  path = new StringBuilder();
		StringBuilder servicos = new StringBuilder();
		
		List<DetalheServico> detalhes = tarefa.getDetalhes();
		tarefa.getDetalhes().forEach(serv->servicos.append(" -").append(serv.getTiposervico().getTipo()));
		
		StringBuilder valores = new StringBuilder();
		
		for(int i = 0; i<detalhes.size(); i++) {
			if(detalhes.get(i).getValorDoServico()!=null || detalhes.get(i).getValorDoServico()!= BigDecimal.ZERO){
				BigDecimal valorDoServico = detalhes.get(i).getValorDoServico().multiply(new BigDecimal(100));
				
				valores
					.append(" -")
					.append(valorDoServico.setScale(0));
			}
		}
		path
			.append(tarefa.getDataCadastro().format(DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER)))
			.append(" -")
			.append(tarefa.getProdutor().getNome().toUpperCase())
			.append(servicos)
			.append(valores)
		;
		log.info("Path do arquivo: {}", path.toString());
		return path.toString();
	}

}
