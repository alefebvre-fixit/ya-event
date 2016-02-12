package com.ya.yaevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EntityAuthorizationException extends RuntimeException {

	private static final long serialVersionUID = -2206493799676016737L;

	public EntityAuthorizationException(String entity, String entityId) {
		super("Authorization denied for " + entity + " '" + entityId + "'.");
	}

}
