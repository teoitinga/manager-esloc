package com.jp.eslocapi.util.exceptions;

public class DoNotCreateFolder extends Exception {
	private static final long serialVersionUID = 668001218064921758L;

	public DoNotCreateFolder() {
		super("Não foi possível criar a pasta para este atendimento.");

	}

}
