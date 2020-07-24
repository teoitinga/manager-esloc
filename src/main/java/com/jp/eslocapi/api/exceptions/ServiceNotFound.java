package com.jp.eslocapi.api.exceptions;

public class ServiceNotFound extends RuntimeException {

	private static final long serialVersionUID = 342296956066915407L;

	public ServiceNotFound() {
		super("Não prestamos este tipo de serviço.");
	}
}
