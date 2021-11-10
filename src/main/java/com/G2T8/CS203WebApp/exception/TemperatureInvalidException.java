package com.G2T8.CS203WebApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TemperatureInvalidException extends RuntimeException{
  public TemperatureInvalidException(double temperature) {
    super("Invalid temperature: "+temperature);
}
}
