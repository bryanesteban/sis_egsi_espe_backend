package com.espe.ListoEgsi.exception;

import org.springframework.http.HttpStatus;

public class CustomExceptionOnly extends RuntimeException {

    private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus;

	public CustomExceptionOnly(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
