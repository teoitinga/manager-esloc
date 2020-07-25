package com.jp.eslocapi.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.DocumentType;

public interface DocumentTypeRepository  extends JpaRepository<DocumentType, Long> {

	Optional<DocumentType> findByAbreviatura(String documentoTipo);

}
