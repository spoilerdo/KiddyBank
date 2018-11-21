package com.kiddybank.Entities;

import javax.persistence.*;

@Entity
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
}
