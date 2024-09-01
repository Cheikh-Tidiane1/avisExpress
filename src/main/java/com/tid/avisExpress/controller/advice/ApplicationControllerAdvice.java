package com.tid.avisExpress.controller.advice;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody ProblemDetail badCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage(), exception);
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "identifiants invalides"
        );
        problemDetail.setProperty("Erreur","nous avons pas pu vous identifier");
        return problemDetail;
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = {MalformedJwtException.class, SignatureException.class})
    public @ResponseBody ProblemDetail badCredentialsException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return  ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "Token invalide"
        );

    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody ProblemDetail handlerException(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(
                FORBIDDEN,
                "Vos droits ne vous permettent pas d'effectuer cette action"
        );

    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String,String> exceptionHandler(){
        return Map.of("error","identifiants invalides !!");
    }
}
