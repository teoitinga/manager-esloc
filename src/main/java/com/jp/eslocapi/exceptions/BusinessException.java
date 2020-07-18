package com.jp.eslocapi.exceptions;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -651971819471641666L;

	public BusinessException(String message) {
		super(message);
	}

}
