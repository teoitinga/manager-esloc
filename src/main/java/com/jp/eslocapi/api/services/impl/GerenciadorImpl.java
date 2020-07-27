package com.jp.eslocapi.api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.dto.DetailServiceDto;
import com.jp.eslocapi.api.dto.ProdutoPostMinDto;
import com.jp.eslocapi.api.dto.Tarefa;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.entities.EnumStatus;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.exceptions.ServiceNotFound;
import com.jp.eslocapi.api.services.Gerenciador;
import com.jp.eslocapi.services.AtendimentoService;
import com.jp.eslocapi.services.TypeServiceService;
import com.jp.eslocapi.util.Gerenciamento;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GerenciadorImpl implements Gerenciador{

	@Autowired
	private ProdutorServiceImpl produtorService;
	
	@Autowired
	private TypeServiceService typeServiceService;
	
	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private Gerenciamento gerenciamentoPasta;

	@Override
	public void buildTarefa(TarefaPostDto dto) {
		Tarefa tarefa = null;
		//obtem o texto sobre observacoes da tarefa
		String observacoes = dto.getObservacoes();
		
		//obtem a lista de pessoal atendido
		//Para cada produtor sera gerada uma tarefa com os dados informados
		List<Persona> produtores = obtemProdutores(dto.getProdutorInfo());
		log.info("Produtores a registrar {}", produtores);
		Persona produtorSolicitante = produtores.get(0);
		//obtem a lista de servicos prestados
		List<Atendimento> servicosPrestados = obtemListaDeAtendimentos(dto.getTipoServico());
		log.info("Atendimentos a registrar {}", dto.getTipoServico().toString());
		
		//Constroi a tarefa e registra no banco de dados
		tarefa = Tarefa.builder()
				.atendimentos(servicosPrestados)
				.produtores(produtores)
				.obervacao(observacoes)
				.build();
		
		tarefaBuilder(tarefa);
		this.gerenciamentoPasta.createFolderToAtendimento(servicosPrestados, produtorSolicitante);
		
	}

	private List<Atendimento> obtemListaDeAtendimentos(List<DetailServiceDto> tipoServico) {
		List<Atendimento> atendimentos = null;
		atendimentos = tipoServico.stream().map(atendimento->transformadtoEmAtendimento(atendimento)).collect(Collectors.toList());
		
		return atendimentos;
	}

	private Atendimento transformadtoEmAtendimento(DetailServiceDto dto) {
		
		Atendimento atendimento = null;
		BigDecimal valorDoServico = new BigDecimal(dto.getValorDoServico());
		log.info("valorDoServico {}", valorDoServico);
		
		String tarefaDescricao = dto.getTarefaDescricao();
		log.info("tarefaDescricao {}", tarefaDescricao);
		
		log.info("emitiuDAE- inline {}", dto.getEmitiuDAE());
		Boolean emitiuDAE = dto.getEmitiuDAE().equals("true") ? true : false;
		log.info("emitiuDAE {}", emitiuDAE);
		
		Boolean emitiuART = dto.getEmitiuART().equals("true") ? true : false;
		log.info("emitiuART {}", emitiuART);
		
		//Configura o Emissor
		Persona emissor = null;
		log.info("emissor {}", emissor);
		
		//Configura o responsável pela execução
		Persona responsavel = null;
		log.info("responsavel {}", responsavel);
		
		//obtem o objeto de Tipo de serviço
		TipoServico tipoDeServico = this.typeServiceService.getByType(dto.getTipoServico()).orElseThrow(() -> new ServiceNotFound());
		log.info("tipoDeServico {}", tipoDeServico);
		
		LocalDate dataDeConclusao = LocalDate.now().plusDays(tipoDeServico.getTempoEstimado());
		
		return atendimento.builder()
				.tiposervico(tipoDeServico)
				.valorDoServico(valorDoServico)
				.tarefaDescricao(tarefaDescricao)
				.emitiuDAE(emitiuDAE)
				.emitiuART(emitiuART)
				.dataConclusaoPrevista(dataDeConclusao)
				.statusTarefa(EnumStatus.INICIADA)
				.emissor(emissor)
				.responsavel(responsavel)
				.build();

	}
	//Registra os dados da Tarefa e retorn a primeira em caso de sucesso
	@Transactional
	private void tarefaBuilder(Tarefa tarefa) {
		
		Tarefa tarefaBuild = null;
		//prepara os campos necessários
		
		//produtores registrados no banco de dados
		List<Persona> savedProdutores = tarefa.getProdutores();

		
		//Atndimentos
		List<Atendimento> atendimentos = tarefa.getAtendimentos();
		log.info("Serviços solicitados {}", atendimentos);
		
		//observações
		String obs = tarefa.getObervacao();
		log.info("Observções {}", obs);
		
		savedProdutores.forEach(produtor->registraAtendimentoPorServico(atendimentos, produtor, obs));

	}

	private void registraAtendimentoPorServico(List<Atendimento> atendimentos, Persona produtor, String obs) {
		
		atendimentos.forEach(atd-> registraAtendimento(atd, produtor, obs));

	}

	private void registraAtendimento(Atendimento atendimento, Persona produtor, String obs) {
		Atendimento atd = new Atendimento();
		Persona prd = produtor;
		atd = atendimento.builder()
				.tiposervico(atendimento.getTiposervico())
				.valorDoServico(atendimento.getValorDoServico())
				.tarefaDescricao(atendimento.getTarefaDescricao())
				.emitiuDAE(atendimento.getEmitiuDAE())
				.emitiuART(atendimento.getEmitiuART())
				.dataConclusaoPrevista(atendimento.getDataConclusaoPrevista())
				.statusTarefa(atendimento.getStatusTarefa())
				.emissor(atendimento.getEmissor())
				.responsavel(atendimento.getResponsavel())
				.observacoes(obs)
				.produtor(prd)
				.build();

		this.atendimentoService.save(atd);
		//define como null para evitar atualizaçoes que não sejam necessárias
		atd = null;
		produtor = null;

	}

	private List<Persona> obtemProdutores(List<ProdutoPostMinDto> produtorInfo) {
		List<Persona> listaDeProdutores = null;
		log.info("Produtores Dto Min {}", produtorInfo);
		
		listaDeProdutores = produtorInfo.stream().map(produtorMin->transoformaEmProdutor(produtorMin)).collect(Collectors.toList());
		
		return listaDeProdutores;
	}

	private Persona transoformaEmProdutor(ProdutoPostMinDto produtorMin) {
		Persona produtor = null;
		//verifica se o produtore já é cadastrado
		try {
			produtor = this.produtorService.getByCpf(produtorMin.getCpf());
		}catch(ProdutorNotFound ex) {
			//caso não seja cadastrado, então faça o registro com os dados mínomos
			produtor = this.produtorService.saveMin(produtorMin);
		}
		
		return produtor;
	}

}
