package com.espe.ListoEgsi.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus;

	public CustomException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
