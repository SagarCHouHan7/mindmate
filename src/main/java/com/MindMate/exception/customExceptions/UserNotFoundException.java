package com.MindMate.exception.customExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        this("user not found");
    }
    public UserNotFoundException(String msg){
        super(msg);
    }
}
