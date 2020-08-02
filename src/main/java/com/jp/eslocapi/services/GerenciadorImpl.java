package com.jp.eslocapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.Configuration;
import com.jp.eslocapi.api.dto.AtendimentoBasicDto;
import com.jp.eslocapi.api.dto.ProdutoPostMinDto;
import com.jp.eslocapi.api.dto.Tarefa;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.entities.EnumStatus;
import com.jp.eslocapi.api.entities.EnumYesNo;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.exceptions.ApiErrors;
import com.jp.eslocapi.api.services.AtendimentoService;
import com.jp.eslocapi.api.services.TypeServiceService;
import com.jp.eslocapi.api.services.impl.ProdutorServiceImpl;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.util.FileUtil;
import com.jp.eslocapi.util.exceptions.DoNotCreateFolder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GerenciadorImpl implements Gerenciador {

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
	@Transactional
	public void buildTarefa(TarefaPostDto dto) {
		Tarefa tarefa = null;
		Atendimento atendimento = new Atendimento();

		
		// Testa consistência das informações
		
		//Define outras variaveis do atendimento pois depende de consultas externas
		
		
		Persona emissor = null;
		Persona responsavel = null;
		
		// verifica se existe pelo menos um produtora atendido
		log.info("Quantidade de produtores: {}", dto.getProdutorInfo().size());

		if (dto.getProdutorInfo() == null || dto.getProdutorInfo().size() == 0) {
			new ApiErrors("Não é possível registrar atendimento sem informar o produtor");
		}
		log.info("Produtores...OK!");

		// verifica se existe pelo menos um atendimento
		log.info("Quantidade de serviços: {}", dto.getTipoServico().size());
		if (dto.getTipoServico() == null || dto.getTipoServico().size() == 0) {
			new ApiErrors("Não é possível registrar atendimento sem informar o serviço prestado");
		}

		log.info("Quantidade de serviços...OK!");

		// verifica se a data é válida
		LocalDate dataDoAtendimento = getDateValid(dto);
		atendimento.setDataAtendimento(dataDoAtendimento);
		
		// obtem o texto sobre observacoes da tarefa e confirua como string vazia caso
		// seja nulo
		String observacoes = dto.getObservacoes();
		if (dto.getObservacoes() == null) {
			observacoes = "";
		}
		atendimento.setObservacoes(observacoes);
		
		// obtem a lista de pessoal atendido
		// Para cada produtor sera gerada uma tarefa com os dados informados
		List<Persona> produtores = obtemProdutores(dto.getProdutorInfo());
		log.info("Produtores configurados {}", produtores);
		
		//registra atendimento para cada produtor da list
		List<Atendimento> servicosPrestados;
		
		//servicosPrestados = criaAtendimentoParaProdutor(produtores, dto.getTipoServico(), atendimento);
		servicosPrestados = obtemListaDeServicos(dto.getTipoServico(), atendimento);
		
		//percorre a lista de produrores
		//rodutores.stream().map(produtor->registrServicoParaProdutor(produtor))
		
		//ao final de todas as variáveis devidamente confiruadas, faz o registro dos atendimentos na variável global
		registraAtendimentos(servicosPrestados, produtores);
		// obtem a lista de servicos prestados
		//List<Atendimento> servicosPrestados = converteParaListaDeAtendimentos(dto.getTipoServico(), atendimento);
		
		//Definie informações para nome da pasta
		String nomeDaPasta = "";
		try {
			nomeDaPasta = resolveNomeDaPasta(servicosPrestados.get(0).getDataAtendimento(), produtores, servicosPrestados);

			tarefa = Tarefa.builder()
					.atendimentos(servicosPrestados)
					.produtores(produtores)
					.obervacao(observacoes)
					.build();

			tarefaBuilder(tarefa);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

		// CRIA a pasta com o nome e caminho definido
		try {
			this.fileUtil.createFolder(nomeDaPasta);
		} catch (DoNotCreateFolder e) {
			// TODO Auto-generated catch block
			throw new BusinessException("Não foi possível crar a pasta para o atendimento!");

		}

	}
	private void registraAtendimentos(List<Atendimento> servicosPrestados, List<Persona> produtores) {
		//percorre a lista de produtores gerando os atendimentos para cada um
		produtores.forEach(produtor->criaAtendimentoParaProdutor(produtor, servicosPrestados));
		
	}
	private void criaAtendimentoParaProdutor(Persona produtor, List<Atendimento> servicos) {
		//percorre a lista de servicos criando um atendimento para cada serviço
		servicos.forEach(servico->criarAtendimento(servico, produtor));
		
	}

	private void criarAtendimento(Atendimento servico, Persona produtor) {
		servico.setProdutor(produtor);
		//salva o atendimento
		log.info("Salvando atendimentos de {}", servico.getProdutor().getNome());
	}
	//retorna a lista de atendimentos, porém o produtor ainda não está configurado
	private List<Atendimento> obtemListaDeServicos(List<AtendimentoBasicDto> servico, Atendimento atd) {
		return servico.stream().map(srv ->criarAtendimento(srv, atd)).collect(Collectors.toList());
	}

	private Atendimento criarAtendimento(AtendimentoBasicDto servico, Atendimento atd) {
		TipoServico tipoDeServico = this.typeServiceService.getByType(servico.getCodDoServico())
				.orElseThrow(()->new BusinessException("Não prestamos este serviço!")) ;
		
		LocalDate dataAtendimento = atd.getDataAtendimento();
		LocalDate dataConclusaoPrevista = dataAtendimento.plusDays(tipoDeServico.getTempoEstimado());
		Persona emissor = atd.getEmissor();
		// define DAE e ART
		EnumYesNo emitiuDAE = EnumYesNo.NAO;

		try {
			emitiuDAE = servico.getEmitiuDae().equals("true") ? EnumYesNo.SIM : EnumYesNo.NAO;

		} catch (Exception e) {

		}
		
		EnumYesNo emitiuART = EnumYesNo.NAO;
		try {
			emitiuART = servico.getEmitiuArt().equals("true") ? EnumYesNo.SIM : EnumYesNo.NAO;

		} catch (Exception e) {

		}
		// define o valor do serviço
		BigDecimal valor;

		try {
			valor = new BigDecimal(servico.getValorDoServico());

		} catch (java.lang.NumberFormatException e) {
			valor = BigDecimal.ZERO;
		} catch (NullPointerException e ) {
			valor = BigDecimal.ZERO;
		}

		Persona responsavel = atd.getResponsavel();
		String observacoes = atd.getObservacoes();
		String tarefaDescricao = servico.getDescricaoDoServico();
		TipoServico tiposervico = tipoDeServico;

		return Atendimento.builder()
				.id(atd.getId())
				.dataAtendimento(dataAtendimento)
				.dataConclusaoPrevista(dataConclusaoPrevista)
				.emissor(emissor)
				.responsavel(responsavel)
				.emitiuART(emitiuART)
				.emitiuDAE(emitiuDAE)
				.tarefaDescricao(tarefaDescricao)
				.tiposervico(tiposervico)
				.valorDoServico(valor)
				.observacoes(observacoes)
				.build();
	}

	// verifica se a data é válida
	private LocalDate getDateValid(TarefaPostDto dto) {
		
		log.info("Data informada: {}", dto.getDataDoAtendimento());
		String cep = "\\d\\d\\d\\d\\d-\\d\\d\\d";
		
		LocalDate dataDoAtendimento = null;
		// tenta obter a data no segundo formato ddmmyyyy
		try {
			dataDoAtendimento = LocalDate.parse(dto.getDataDoAtendimento(),
					(this.folderDate.frontDateTimeFormater()));
		} catch (DateTimeParseException e) {
			
			// se deu erro nas 02 situações então a variável continua null
			// se a variável é null, antão é setada com a data atual
		}
		
		// tenta obter a data no formato string convencional
		try {    
			dataDoAtendimento = LocalDate.parse(dto.getDataDoAtendimento());
		} catch (DateTimeParseException e) {

		} catch( NullPointerException ex) {
			//dataDoAtendimento = LocalDate.now();
		}

		// tenta obter a data no segundo formato dd/mm/yyyy
		try {
			dataDoAtendimento = LocalDate.parse(dto.getDataDoAtendimento(),
					(this.folderDate.viewDateTimeFormater()));
		} catch (DateTimeParseException e) {
			
			// se deu erro nas 02 situações então a variável continua null
			// se a variável é null, antão é setada com a data atual
		} catch( NullPointerException ex) {
			dataDoAtendimento = LocalDate.now();
		}

		
		//verifica se o atendimento foi agendado para mais de 10 dias
		if(dataDoAtendimento.isAfter(LocalDate.now().plusDays(10))) {
			log.info("Data informada agendada para mais de 10 dias -> {}", dto.getDataDoAtendimento());
			throw new BusinessException("Não é possível registrar atendimento para daqui a mais de 10 dias.");
		}
		
		//verifica se o atendimento ocorreu a mais de 30 dias
		if(dataDoAtendimento.isBefore(LocalDate.now().minusDays(30))) {
			log.info("Data informada ocorrida a mais de 30 dias -> {}", dto.getDataDoAtendimento());
			throw new BusinessException("Não é possível registrar atendimento que ocorreu a mais de 30 dias.");
		}
		
		log.info("Data informada ...OK!");
		return dataDoAtendimento;
	}


	private Atendimento converteParaAtendimento(AtendimentoBasicDto servico, Atendimento atd) {
		TipoServico tipoDeServico = this.typeServiceService.getByType(servico.getCodDoServico())
				.orElseThrow(()->new BusinessException("Não prestamos este serviço!")) ;
		
		LocalDate dataAtendimento = atd.getDataAtendimento();
		LocalDate dataConclusaoPrevista = dataAtendimento.plusDays(tipoDeServico.getTempoEstimado());
		Persona emissor = atd.getEmissor();
		EnumYesNo emitiuART = atd.getEmitiuART();
		EnumYesNo emitiuDAE = atd.getEmitiuDAE();
		Persona responsavel = atd.getResponsavel();
		String observacoes = atd.getObservacoes();
		String tarefaDescricao = servico.getDescricaoDoServico();
		Persona produtor = atd.getProdutor();
		TipoServico tiposervico = tipoDeServico;
		BigDecimal valorDoServico = atd.getValorDoServico();
		return Atendimento.builder().produtor(produtor).dataAtendimento(dataAtendimento)
				.dataConclusaoPrevista(dataConclusaoPrevista).emissor(emissor).responsavel(responsavel)
				.emitiuART(emitiuART).emitiuDAE(emitiuDAE).tarefaDescricao(tarefaDescricao).tiposervico(tiposervico)
				.valorDoServico(valorDoServico).observacoes(observacoes).build();
	}
	
	private String resolveNomeDaPasta(LocalDate dataDoAtendimento, List<Persona> produtores, List<Atendimento> servicosPrestados) {
		StringBuilder fileName = new StringBuilder();

		log.info("Produtor selecionado: {}", produtores.get(0));
		log.info("Servicos : {} ", servicosPrestados);
		// Formato: ANO-MES-DIA -NOME_DO_CLIENTE -CODIGO_DE_BUSCA -SERV_01 -VL_SERV_01
		// -SERV_02 -VL_SERV_02 -DAE -ART
		// DAE E ART somente se foi emitida e não quitada.
		// 1a Parte: DATA
		// String dataAtual =
		// LocalDate.now().format(folderDate.folderDateTimeFormater());
		// fileName.append(dataAtual);
		// 3a Parte: -CODIGO_DE_BUSCA COMPOSTA PELO CPFDIAMESANO
		LocalDateTime dataDoAtendimentoTime = dataDoAtendimento.atTime(0, 0); 
try {
	fileName.append(dataDoAtendimentoTime.format(folderDate.keyDateTimeFormater()));
	
} catch(java.time.temporal.UnsupportedTemporalTypeException ex) {
	
}
		fileName.append(produtores.get(0).getCpf());

		// 2a Parte: NOME_DO_CLIENTE
		String nomeDoProdutor = produtores.get(0).getNome().toUpperCase();
		fileName.append(" -");
		fileName.append(nomeDoProdutor);

		// 4a Parte: SERVICOS
		for (int i = 0; i < servicosPrestados.size(); i++) {
			fileName.append(" -");
			fileName.append(servicosPrestados.get(i).getTiposervico().getTipo());

			EnumYesNo emitiuArt = servicosPrestados.get(i).getEmitiuART();
			EnumYesNo emitiuDae = servicosPrestados.get(i).getEmitiuDAE();

			// Confere se é maior que ZERO, caso positivo insere o vallor na pasta
			if (servicosPrestados.get(i).getValorDoServico().compareTo(BigDecimal.ZERO) > 0) {
				fileName.append(" -");
				fileName.append(servicosPrestados.get(i).getValorDoServico());
			}
			// Se não for emitido o DAE, é necessário informar na pasta
			if (emitiuDae != EnumYesNo.SIM) {
				fileName.append(" -");
				fileName.append("DAE");
			}
			// Se não for emitida a ART, é necessário informar na pasta
			if (emitiuArt != EnumYesNo.SIM) {
				fileName.append(" -");
				fileName.append("ART");
			}

		}
		log.info("Criando pasta: {}", fileName.toString());
		return fileName.toString();
	}

	// Registra os dados da Tarefa e retorn a primeira em caso de sucesso
	@Transactional
	private void tarefaBuilder(Tarefa tarefa) {
		// prepara os campos necessários

		List<Persona> savedProdutores = null;
		// produtores registrados no banco de dados
		try {
			savedProdutores = tarefa.getProdutores();

		} catch (Exception ex) {

		}

		// Atendimentos

		List<Atendimento> atendimentos = tarefa.getAtendimentos();
		// observações
		String obs = tarefa.getObervacao();

		savedProdutores.forEach(produtor -> registraAtendimentoPorServico(atendimentos, produtor, obs));

	}

	private void registraAtendimentoPorServico(List<Atendimento> atendimentos, Persona produtor, String obs) {

		atendimentos.forEach(atd -> registraAtendimento(atd, produtor, obs));

	}

	private void registraAtendimento(Atendimento atendimento, Persona produtor, String obs) {
		Atendimento atd = new Atendimento();
		Persona prd = produtor;
		atd = Atendimento.builder()
				.tiposervico(atendimento.getTiposervico())
				.dataAtendimento(atendimento.getDataAtendimento())
				.responsavel(atendimento.getResponsavel())
				.emissor(atendimento.getEmissor())
				.valorDoServico(atendimento.getValorDoServico()).tarefaDescricao(atendimento.getTarefaDescricao())
				.emitiuDAE(atendimento.getEmitiuDAE())
				.emitiuART(atendimento.getEmitiuART())
				.dataConclusaoPrevista(atendimento.getDataConclusaoPrevista())
				.statusTarefa(atendimento.getStatusTarefa()).emissor(atendimento.getEmissor())
				.responsavel(atendimento.getResponsavel()).statusTarefa(EnumStatus.INICIADA).observacoes(obs)
				.produtor(prd).build();
		log.info("Registrando atendimentos {}", atd);
		atd = this.atendimentoService.save(atd);
		log.info("Atendimento registrado {}", atd);

		if (atd == null) {
			
			throw new BusinessException("Não houve registro, ocorreu um erro na validação das informações.");
		}
		// define como null para evitar atualizaçoes que não sejam necessárias
		atd = null;
		prd = null;


	}

	private List<Persona> obtemProdutores(List<ProdutoPostMinDto> produtorInfo) {
		List<Persona> listaDeProdutores = null;
		log.info("Produtores Dto Min {}", produtorInfo);

		listaDeProdutores = produtorInfo.stream().map(produtorMin -> transoformaEmProdutor(produtorMin))
				.collect(Collectors.toList());

		return listaDeProdutores;
	}

	private Persona transoformaEmProdutor(ProdutoPostMinDto produtorMin) {
		Persona produtor = null;
		// verifica se o produtore já é cadastrado
		log.info("Modificando produtor: {}", produtorMin);
		
		//Verifica se existe este produtor, caso negativo, a variavel é configurada como null
		produtor = this.produtorService.whatIsCpf(produtorMin.getCpf());
		if (produtor != null) {
			log.info("Produtor modificado...retornando: {}", produtor);
			return produtor;
		} else {
			produtor = this.produtorService.saveMin(produtorMin);
			log.info("Produtor modificado...retornando: {}", produtor);
			return produtor;
			
		}

	}

}
