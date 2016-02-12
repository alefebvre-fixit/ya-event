package com.ya.yaevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 237000380885909940L;

	public EntityNotFoundException(String entity, String entityId) {
		super("could not find " + entity + " '" + entityId + "'.");
	}

}
