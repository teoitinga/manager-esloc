package com.jp.eslocapi.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.TipoServico;

public interface TypeServiceRepository extends JpaRepository<TipoServico, Long> {
	
	Optional<TipoServico> findByTipo(String tipo);
	
}
