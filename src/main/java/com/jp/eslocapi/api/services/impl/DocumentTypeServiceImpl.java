package com.jp.eslocapi.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.DocumentType;
import com.jp.eslocapi.api.exceptions.DocumentNotFoundException;
import com.jp.eslocapi.api.repositories.DocumentTypeRepository;
import com.jp.eslocapi.api.services.DocumentTypeService;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService{
	
	@Autowired
	private DocumentTypeRepository repository;

	@Override
	public DocumentType save(DocumentType documentType) {
		return this.repository.save(documentType);
	}

	@Override
	public List<DocumentType> findAll() {
		return Optional.of(this.repository.findAll()).orElseThrow(()-> new DocumentNotFoundException());
	}

	@Override
	public DocumentType findByTipo(String documentoTipo) {
		return this.repository.findByAbreviatura(documentoTipo).orElseThrow(()-> new DocumentNotFoundException());
	}
	@Override
	public boolean isContaining() {
		return this.repository.findAll().size() >= 0 ? true : false;
	}

	@Override
	public void deleteAll() {
		this.repository.deleteAll();
		
	}

}
