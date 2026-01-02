package com.espe.ListoEgsi.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

	 int statusCode;
	 Date timestamp;
	 String message;
	 String description;
}
