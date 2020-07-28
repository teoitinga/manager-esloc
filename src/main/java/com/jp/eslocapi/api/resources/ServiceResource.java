package com.jp.eslocapi.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.ServicoDto;
import com.jp.eslocapi.api.services.TypeServiceService;

@RestController
@RequestMapping("api/v1/services")
public class ServiceResource {
	
	@Autowired
	TypeServiceService service;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ServicoDto> getAllDocuments() {
		return this.service.findAll();
	}
	
}
