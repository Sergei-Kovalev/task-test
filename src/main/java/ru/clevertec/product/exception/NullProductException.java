package ru.clevertec.product.exception;

import java.util.UUID;

public class NullProductException extends RuntimeException {
    public NullProductException () {
        super("Product cannot be null");
    }
}
