package com.jp.eslocapi.util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.jp.eslocapi.util.exceptions.DoNotCreateFolder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileUtil {
	
	@Value("${servicos.atendimentos.raiz}")
	private String raiz;
	
	public void salvar(MultipartFile arquivo, String diretorio, String nome) {
		String extensaoOriginal = arquivo.getOriginalFilename().split("[.]")[1];
		
		Path diretorioPath = Paths.get(this.raiz, diretorio);
		
		Path arquivoPath = diretorioPath.resolve(nome + "." + extensaoOriginal);
		
		log.info("Arquivo enviado: {}", arquivoPath.getFileName());
		log.info("Diretório informado: {}", diretorioPath);

		try {
			Files.createDirectories(diretorioPath);
			arquivo.transferTo(arquivoPath.toFile());			
		} catch (IOException e) {
			throw new RuntimeException("Problemas na tentativa de salvar arquivo.", e);
		}		
	}
	//Cria uma pasta com o nome informado. A pasta é criada no diretorio raiz do sistema
	public void createFolder(String folderName) throws DoNotCreateFolder {
		log.info("Criando a pasta para o atendimentos {}", folderName);
		
		Path diretorioPath = Paths.get(this.raiz, folderName);
		try {
			log.info("Criando a pasta para o atendimentos {}", diretorioPath.getFileName());
			log.info("Criando a pasta para o atendimentos {}", diretorioPath.getParent());
			Files.createDirectories(diretorioPath);
			
		} catch (IOException e) {
			
			throw new DoNotCreateFolder();
		}		
	}
	
}
