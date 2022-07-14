package com.wlp.ecm.weap.exception;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wlp.ecm.weap.config.AppConfiguration;

public class WeapException extends Exception {

	private static final long serialVersionUID = -4413118054623709068L;
    private String errorCode = null;
    private String message = null;

	public WeapException(String errorCode) {
		this.errorCode = errorCode;
		this.message = findExceptionMessage(errorCode, null);
	}

	public WeapException(String errorCode, Object...args) {
		this.errorCode = errorCode;
		this.message = findExceptionMessage(errorCode, args);
	}

	public WeapException(String errorCode, Throwable cause) {
		super(cause);
		
		this.errorCode = errorCode;
		this.message = findExceptionMessage(errorCode, null);
	}

	public WeapException(String errorCode, Throwable cause, Object...args) {
		super(cause);
		
		this.errorCode = errorCode;
		this.message = findExceptionMessage(errorCode, args);
	}

	public String getErrorCode() {
		return this.errorCode;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	protected String findExceptionMessage(String errorCode, Object[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		String message = null;
		
		try {
			String fullErrorCode = this.getClass().getPackage().getName() + "." + errorCode;
			message = ctx.getMessage(fullErrorCode, args, Locale.getDefault());
		} catch (NoSuchMessageException e) {
			// e.printStackTrace();
			message = "Could not find message for error code: " + errorCode;
		}
		return message;
	}
}
