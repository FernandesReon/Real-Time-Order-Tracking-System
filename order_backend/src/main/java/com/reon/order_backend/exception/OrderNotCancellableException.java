package com.reon.order_backend.exception;

public class OrderNotCancellableException extends RuntimeException {
    public OrderNotCancellableException(String message) {
        super(message);
    }
}
