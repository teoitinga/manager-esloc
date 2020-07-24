package com.jp.eslocapi.api.exceptions;

public class TarefaNotFoundException  extends RuntimeException {

	private static final long serialVersionUID = -1063849724657037342L;

	public TarefaNotFoundException() {
		super("Não foi encontrada esta tarefa.");
	}
}
