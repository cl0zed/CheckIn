package com.example.teamlabproject.events;


public class ResponseMessageEvent {

    private String errorMessage;

    public ResponseMessageEvent(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
