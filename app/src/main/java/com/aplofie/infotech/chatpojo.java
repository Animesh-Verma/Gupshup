package com.aplofie.infotech;

public class chatpojo {

    private String message;
    private String sender;
    private String reciever;
    private String timestamp;



    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";


    public chatpojo(){}

    public chatpojo (String timestamp,String sender ,String reciever ,String message)
    {
        this.message = message;
       this.timestamp = timestamp;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
