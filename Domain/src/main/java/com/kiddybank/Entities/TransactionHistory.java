package com.kiddybank.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "senderID")
    private int senderId;
    @Column(name = "ReceiverID")
    private int receiverId;
    @Column(name = "TransactionAmount")
    private Float transactionAmound;

    public TransactionHistory() {}

    public TransactionHistory(int senderId, int receiverId, Float transactionAmound) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transactionAmound = transactionAmound;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public Float getTransactionAmound() {
        return transactionAmound;
    }
}
