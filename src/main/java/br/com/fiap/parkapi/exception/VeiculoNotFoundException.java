package br.com.fiap.parkapi.exception;

public class VeiculoNotFoundException extends RuntimeException {
    public VeiculoNotFoundException(String message) {
        super(message);
    }
}
