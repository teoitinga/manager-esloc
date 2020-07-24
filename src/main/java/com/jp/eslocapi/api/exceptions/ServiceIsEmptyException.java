package com.jp.eslocapi.api.exceptions;

public class ServiceIsEmptyException extends RuntimeException {

	public ServiceIsEmptyException() {
		super("Não se registra atendimentos sem serviços.");
	}
}
