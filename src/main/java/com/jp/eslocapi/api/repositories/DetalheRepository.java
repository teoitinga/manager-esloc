package com.jp.eslocapi.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.DetalheServico;

public interface DetalheRepository  extends JpaRepository<DetalheServico, Long> {

}
