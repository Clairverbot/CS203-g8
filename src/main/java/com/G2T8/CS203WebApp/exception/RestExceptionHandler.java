package com.G2T8.CS203WebApp.Exception;

import javax.validation.ConstraintViolationException;
// import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
// import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception, ServletWebRequest webRequest)
            throws IOException {
        webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
