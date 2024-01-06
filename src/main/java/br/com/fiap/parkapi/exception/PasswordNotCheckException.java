package br.com.fiap.parkapi.exception;

public class PasswordNotCheckException extends RuntimeException {
    public PasswordNotCheckException(String message) {
        super(message);
    }
}
