package com.jp.eslocapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.Configuration;
import com.jp.eslocapi.api.dto.DetailServiceDto;
import com.jp.eslocapi.api.dto.ProdutoPostMinDto;
import com.jp.eslocapi.api.dto.Tarefa;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.entities.EnumStatus;
import com.jp.eslocapi.api.entities.EnumYesNo;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.exceptions.ServiceNotFound;
import com.jp.eslocapi.api.services.AtendimentoService;
import com.jp.eslocapi.api.services.TypeServiceService;
import com.jp.eslocapi.api.services.impl.ProdutorServiceImpl;
import com.jp.eslocapi.util.FileUtil;
import com.jp.eslocapi.util.exceptions.DoNotCreateFolder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GerenciadorImpl implements Gerenciador{

	@Value("${servicos.atendimentos.raiz}")
	private static String PATH_ROOT;

	@Value("${esloc.date.folder}")
	private static String DATA_FORMAT_FOLDER;
	
	@Autowired
	private ProdutorServiceImpl produtorService;
	
	@Autowired
	private TypeServiceService typeServiceService;
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@Autowired
	private Configuration folderDate;

	@Autowired
	private FileUtil fileUtil;
	
	@Override
	public void buildTarefa(TarefaPostDto dto) {
		Tarefa tarefa = null;
		//obtem o texto sobre observacoes da tarefa
		String observacoes = dto.getObservacoes();
		
		//obtem a lista de pessoal atendido
		//Para cada produtor sera gerada uma tarefa com os dados informados
		List<Persona> produtores = obtemProdutores(dto.getProdutorInfo());

		//obtem a lista de servicos prestados
		List<Atendimento> servicosPrestados = obtemListaDeAtendimentos(dto.getTipoServico());
		
		String nomeDaPasta = resolveNomeDaPasta(produtores, servicosPrestados);
		
		tarefa = Tarefa.builder()
				.atendimentos(servicosPrestados)
				.produtores(produtores)
				.obervacao(observacoes)
				.build();
		
		tarefaBuilder(tarefa);
		
		//CRIA a pasta com o nome e caminho definido
		try {
			this.fileUtil.createFolder(nomeDaPasta);
		} catch (DoNotCreateFolder e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String resolveNomeDaPasta(List<Persona> produtores, List<Atendimento> servicosPrestados) {
		StringBuilder fileName = new  StringBuilder();
		
		log.info("Produtor selecionado: {}", produtores.get(0));
		log.info("Servicos : {} ", servicosPrestados);
		//Formato: ANO-MES-DIA -NOME_DO_CLIENTE -CODIGO_DE_BUSCA -SERV_01 -VL_SERV_01 -SERV_02 -VL_SERV_02 -DAE -ART
		//DAE E ART somente se foi emitida e não quitada.
		//1a Parte: DATA
		//String dataAtual = LocalDate.now().format(folderDate.folderDateTimeFormater());
		//fileName.append(dataAtual);
		//3a Parte: -CODIGO_DE_BUSCA COMPOSTA PELO CPFDIAMESANO

		fileName.append(LocalDateTime.now().format(folderDate.keyDateTimeFormater()));		
		fileName.append(produtores.get(0).getCpf());

		//2a Parte: NOME_DO_CLIENTE
		String nomeDoProdutor = produtores.get(0).getNome().toUpperCase();
		fileName.append(" -");
		fileName.append(nomeDoProdutor);
		
		
		//4a Parte: SERVICOS
		for(int i = 0; i< servicosPrestados.size();i++) {
			fileName.append(" -");
			fileName.append(servicosPrestados.get(i).getTiposervico().getTipo());			

			EnumYesNo emitiuArt = servicosPrestados.get(i).getEmitiuART();
			EnumYesNo emitiuDae = servicosPrestados.get(i).getEmitiuDAE();
			
			//Confere se é maior que ZERO, caso positivo insere o vallor na pasta
			if(servicosPrestados.get(i).getValorDoServico().compareTo(BigDecimal.ZERO) > 0) {
				fileName.append(" -");
				fileName.append(servicosPrestados.get(i).getValorDoServico());			
			}			
			//Se não for emitido o DAE, é necessário informar na pasta 
			if(emitiuDae != EnumYesNo.SIM) {
				fileName.append(" -");
				fileName.append("DAE");			
			}
			//Se não for emitida a ART, é necessário informar na pasta 
			if(emitiuArt != EnumYesNo.SIM) {
				fileName.append(" -");
				fileName.append("ART");			
			}
			
		}
		log.info("Criando pasta: {}", fileName.toString());
		return fileName.toString();
	}

	private List<Atendimento> obtemListaDeAtendimentos(List<DetailServiceDto> tipoServico) {
		List<Atendimento> atendimentos = null;
		atendimentos = tipoServico.stream().map(atendimento->transformadtoEmAtendimento(atendimento)).collect(Collectors.toList());
		
		return atendimentos;
	}

	private Atendimento transformadtoEmAtendimento(DetailServiceDto dto) {
		BigDecimal valorDoServico;
		try {
			valorDoServico = new BigDecimal(dto.getValorDoServico());
			
		}catch (java.lang.NumberFormatException e) {
			valorDoServico = BigDecimal.ZERO;
		}
		
		String tarefaDescricao = dto.getTarefaDescricao();
		
		EnumYesNo emitiuDAE = dto.getEmitiuDAE().equals("true") ? EnumYesNo.SIM : EnumYesNo.NAO;
		
		EnumYesNo emitiuART = dto.getEmitiuART().equals("true") ? EnumYesNo.SIM : EnumYesNo.NAO;
		
		//Configura o Emissor
		Persona emissor = null;
		log.info("emissor {}", emissor);
		
		//Configura o responsável pela execução
		Persona responsavel = null;
		log.info("responsavel {}", responsavel);
		
		//obtem o objeto de Tipo de serviço
		TipoServico tipoDeServico = this.typeServiceService.getByType(dto.getTipoServico().toUpperCase()).orElseThrow(() -> new ServiceNotFound());
		log.info("tipoDeServico {}", tipoDeServico);
		
		LocalDate dataDeConclusao = LocalDate.now().plusDays(tipoDeServico.getTempoEstimado());
		
		return Atendimento.builder()
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
		//prepara os campos necessários
		
		//produtores registrados no banco de dados
		List<Persona> savedProdutores = tarefa.getProdutores();

		//Atendimentos
		List<Atendimento> atendimentos = tarefa.getAtendimentos();
		
		//observações
		String obs = tarefa.getObervacao();
		
		savedProdutores.forEach(produtor->registraAtendimentoPorServico(atendimentos, produtor, obs));

	}

	private void registraAtendimentoPorServico(List<Atendimento> atendimentos, Persona produtor, String obs) {
		
		atendimentos.forEach(atd-> registraAtendimento(atd, produtor, obs));

	}

	private void registraAtendimento(Atendimento atendimento, Persona produtor, String obs) {
		Atendimento atd = new Atendimento();
		Persona prd = produtor;
		atd = Atendimento.builder()
				.tiposervico(atendimento.getTiposervico())
				.valorDoServico(atendimento.getValorDoServico())
				.tarefaDescricao(atendimento.getTarefaDescricao())
				.emitiuDAE(atendimento.getEmitiuDAE())
				.emitiuART(atendimento.getEmitiuART())
				.dataConclusaoPrevista(atendimento.getDataConclusaoPrevista())
				.statusTarefa(atendimento.getStatusTarefa())
				.emissor(atendimento.getEmissor())
				.responsavel(atendimento.getResponsavel())
				.statusTarefa(EnumStatus.INICIADA)
				.observacoes(obs)
				.produtor(prd)
				.build();

		this.atendimentoService.save(atd);
		//define como null para evitar atualizaçoes que não sejam necessárias
		atd = null;
		prd = null;

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
