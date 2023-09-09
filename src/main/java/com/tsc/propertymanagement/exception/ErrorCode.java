package com.tsc.propertymanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * This Class will contain all the error codes along with the http status that will be passed to the Service Exception
 * corresponding to the Error code
 */
@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND("2001", HttpStatus.NOT_FOUND),
    EQUIPMENT_NOT_FOUND("2002", HttpStatus.NOT_FOUND),
    MAINTENANCE_RECORD_NOT_FOUND("2003", HttpStatus.NOT_FOUND),
    ENTITY_ALREADY_EXISTS_USER("2004", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final HttpStatus httpStatus;

    ErrorCode(String errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
