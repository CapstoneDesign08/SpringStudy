package com.capstone08.springstudy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FoundBlankException extends Exception{
    public FoundBlankException() {
        super("Threre was Blanks");
        System.out.print("There was a Exception ");}
}