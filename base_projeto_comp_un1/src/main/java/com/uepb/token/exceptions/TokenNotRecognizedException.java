package com.uepb.token.exceptions;

public class TokenNotRecognizedException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;

	public TokenNotRecognizedException(String message){
        super("Erro Léxico: " + message);
    }

}
