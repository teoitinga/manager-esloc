package com.jp.eslocapi.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jp.eslocapi.api.entities.DocumentType;
import com.jp.eslocapi.api.entities.Tarefa;
import com.jp.eslocapi.services.DocumentTypeService;
import com.jp.eslocapi.services.TarefaService;
import com.jp.eslocapi.util.FileUtil;
import com.jp.eslocapi.util.Gerenciamento;

@RestController
@RequestMapping("api/v1/upload")
public class DocumentoResource {

	@Autowired
	private FileUtil file;
	
	@Autowired
	private Gerenciamento gerenciamento;
	
	@Autowired
	private TarefaService tarefaService;
	
	@Autowired
	private DocumentTypeService documentTypeService;

	@PostMapping
	public void upload(@RequestParam MultipartFile documento, @RequestParam String documentoTipo,
			@RequestParam String idTarefa) {

		//String tipoDeDocumento;
		DocumentType document = this.documentTypeService.findByTipo(documentoTipo);
		
		Tarefa tarefa = this.tarefaService.getById(idTarefa);

		String nomeDoDiretorio = this.gerenciamento.obtemNomeDePastaDoProdutor(tarefa);

		String nomeDoArquivo = this.gerenciamento.obtemNomeDeArquivo(tarefa, document);

		file.salvar(documento, nomeDoDiretorio, nomeDoArquivo);

	}

}
