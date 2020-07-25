package com.jp.eslocapi.api.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jp.eslocapi.util.FileUtil;

	@RestController
	@RequestMapping("api/v1/documentos")
	public class DocumentoResource {
		
		@Autowired
		private FileUtil file;
		
		@PostMapping
		public void upload(@RequestParam MultipartFile documento) {
			file.salvar(documento,"fulana","DDA");
			
		}

	}
