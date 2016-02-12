package com.ya.yaevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class YaAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = -7153016003753859020L;

	public YaAuthenticationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public YaAuthenticationException(String msg) {
		super(msg);
	}
	
}
