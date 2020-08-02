package com.jp.eslocapi.api.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.validation.BindingResult;

import com.jp.eslocapi.exceptions.BusinessException;

public class ApiErrors {
	private List<String> errors;
	
	public ApiErrors(BindingResult resultErrors) {
		this.errors = new ArrayList<>();
		resultErrors.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
	}

	public ApiErrors(BusinessException ex) {
		this.errors = Arrays.asList(ex.getMessage());
	}

	public ApiErrors(ProdutorNotFound ex) {
		this.errors = Arrays.asList(ex.getMessage());
	}

	public ApiErrors(String error) {
		this.errors = Arrays.asList(error);
	}

	public ApiErrors(Set<ConstraintViolation<?>> constraintViolations) {
		this.errors = new ArrayList<>();
		constraintViolations.stream().map(violation -> this.errors.add(violation.getMessage()));
	}

	public List<String> getErrors() {
		return errors;
	}
	
}
