package com.kiddybank.Wrappers;

//wrapper voor het opvragen van de body in de post call transaction, hiermee kunnen we alle variabelen opvragen hoeven we niet onze eigen.
//source : https://stackoverflow.com/questions/5726583/spring-rest-multiple-requestbody-parameters-possible
public class TransactionResponse {
    int senderID;
    int receiverID;
    float price;

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public float getPrice() {
        return price;
    }

    //TODO: heb je perse de setters nodig??
}
