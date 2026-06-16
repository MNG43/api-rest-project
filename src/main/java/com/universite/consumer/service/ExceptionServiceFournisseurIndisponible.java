package com.universite.consumer.service;

public class ExceptionServiceFournisseurIndisponible extends RuntimeException {

    public ExceptionServiceFournisseurIndisponible(String message) {
        super(message);
    }

    public ExceptionServiceFournisseurIndisponible(String message, Throwable cause) {
        super(message, cause);
    }
}
