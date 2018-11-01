package com.kiddybank.Wrappers;

//wrapper for linking bank accounts to your own bank account ( own account comes from principal claim in different parameter ).
public class linkRequestModel {
    private int otherID;
    private int bankID;

    public linkRequestModel() {

    }


    public int getOtherID() {
        return otherID;
    }

    public void setOtherID(int otherID) {
        this.otherID = otherID;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }
}
