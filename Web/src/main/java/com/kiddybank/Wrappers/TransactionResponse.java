package com.kiddybank.Wrappers;

//wrapper voor het opvragen van de body in de post call transaction, hiermee kunnen we alle variabelen opvragen hoeven we niet onze eigen.
//source : https://stackoverflow.com/questions/5726583/spring-rest-multiple-requestbody-parameters-possible
public class TransactionResponse {
    private int senderId;
    private int receiverId;
    private float price;

    public TransactionResponse(){}

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public float getPrice() {
        return price;
    }
}
