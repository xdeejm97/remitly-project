package com.task.remitlyproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when swift code already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class SwiftCodeAlreadyExistsException extends RuntimeException {

  public SwiftCodeAlreadyExistsException(String swiftCode) {
    super(String.format("Swift code already exists: %s", swiftCode));
  }
}
