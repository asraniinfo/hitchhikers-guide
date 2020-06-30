package com.cgm.assignment.hitchhikersguide.exception;

public class LengthExceededException extends RuntimeException {
    public LengthExceededException(String msg){
        super(msg);
    }
}
