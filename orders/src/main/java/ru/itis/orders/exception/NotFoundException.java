package ru.itis.orders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class NotFoundException extends HttpClientErrorException {
    public NotFoundException(String statusText) {
        super(HttpStatus.NOT_FOUND, statusText);
    }
}
