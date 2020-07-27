package com.jp.eslocapi.util;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jp.eslocapi.api.dto.Tarefa;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.entities.DocumentType;
import com.jp.eslocapi.api.entities.Persona;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Gerenciamento {
	
	@Value("${esloc.date.folder}")
	private String DATA_FORMAT_FOLDER;// = "yyyy-MM-dd";

	@Value("${servicos.atendimentos.raiz}")
	private String PATH_ROOT;// = "yyyy-MM-dd";
	
	public String obtemNomeDeArquivo(Tarefa tarefa, DocumentType document) {
		StringBuilder arquivo = new StringBuilder();
		arquivo.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER)))
		.append(" ")
		.append(document.getAbreviatura())
		.append(" ")
		.append(tarefa.getProdutores().get(0)
				.getNome().toUpperCase())
		;
		
		log.info("Nome do arquivo: {}", arquivo.toString());
		
		return arquivo.toString();
	}
	public String obtemNomeDePastaDoProdutor(Tarefa tarefa) {
		StringBuilder  path = new StringBuilder();
		StringBuilder servicos = new StringBuilder();
		
		List<Atendimento> detalhes = tarefa.getAtendimentos();
		tarefa.getAtendimentos().forEach(serv->servicos.append(" -").append(serv.getTiposervico().getTipo()));
		
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
			.append(tarefa.getAtendimentos().get(0).getDataCadastro().format(DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER)))
			.append(" -")
			.append(tarefa.getAtendimentos().get(0).getProdutor().getNome().toUpperCase())
			.append(servicos)
			.append(valores)
		;
		log.info("Path do arquivo: {}", path.toString());
		return path.toString();
	}

	public void createFolderToAtendimento(List<Atendimento> servicosPrestados, Persona produtorSolicitante) {
		//Constroi a tarefa e registra no banco de dados
		Tarefa tarefa = Tarefa.builder()
				.atendimentos(servicosPrestados)
				.build();

		String nomeDaPasta = buildFolderName(tarefa);	
		log.info("Nome da pasta {}", nomeDaPasta);
	}

	private String buildFolderName(Tarefa tarefa) {
		StringBuilder  path = new StringBuilder();
		
		path.append(PATH_ROOT);
		
		//obtem o os servico prestados
		List<Atendimento> atd = tarefa.getAtendimentos();

		//obtem a data
//		String dataAtendimento = atd.get(0).getDataCadastro().format(DateTimeFormatter.ofPattern(DATA_FORMAT_FOLDER));

//		//obtem o primeiro produtor
//		Persona produtor = tarefa.getProdutores().get(0);
//		path.append(" -");
//		path.append(produtor.getNome().toUpperCase());
		
		return path.toString();
		
		
	}

}
