package br.com.fiap.parkapi.exception;

public class ParkingNotAllowedException extends RuntimeException {

    public ParkingNotAllowedException(String message) {
        super(message);
    }
}
