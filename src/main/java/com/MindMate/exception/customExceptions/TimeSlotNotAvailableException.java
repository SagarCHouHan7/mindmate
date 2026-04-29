package com.MindMate.exception.customExceptions;

public class TimeSlotNotAvailableException extends RuntimeException{
    public TimeSlotNotAvailableException(){
        this("requested time slot is not available for you");
    }
    public TimeSlotNotAvailableException(String msg){
        super(msg);
    }
}
