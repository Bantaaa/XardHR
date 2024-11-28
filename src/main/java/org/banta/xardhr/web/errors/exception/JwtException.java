package org.banta.xardhr.web.errors.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtException extends RuntimeException {
    private final HttpStatus status;

    public JwtException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
