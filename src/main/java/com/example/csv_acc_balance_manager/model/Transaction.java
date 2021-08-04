package com.example.csv_acc_balance_manager.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    @Column(nullable = false)
    private int accNumber;
    @Column(nullable = false)
    private String beneficiary;
    @Column(nullable = false)
    private String operationDate;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private String currency;

    private String comment;
    private double finalBalance;

}
