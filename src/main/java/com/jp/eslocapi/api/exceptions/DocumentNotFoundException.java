package com.jp.eslocapi.api.exceptions;

public class DocumentNotFoundException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1577462485304183088L;

	public DocumentNotFoundException() {
		super("Nenhum documento encontrado.");
	}
}
