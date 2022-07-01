package com.etschel.ethr.abidemo.controller.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="ABI not found")
public class EntityNotFoundException extends RuntimeException {
    //
}
