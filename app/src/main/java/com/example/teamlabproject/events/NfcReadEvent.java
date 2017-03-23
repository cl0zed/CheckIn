package com.example.teamlabproject.events;

/**
 * Created by b00blik on 14.04.2016.
 */
public class NfcReadEvent {

    private String dataString;

    public String getDataString(){
        return this.dataString;
    }

    public NfcReadEvent(){

    }

    public NfcReadEvent(String dataString){
        this.dataString = dataString;
    }

}
