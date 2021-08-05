package com.example.csv_acc_balance_manager.model;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Transaction() {
    }

    public Transaction(Long transactionId, int accNumber, String beneficiary, String operationDate, double amount, String currency, String comment) {
        this.transactionId = transactionId;
        this.accNumber = accNumber;
        this.beneficiary = beneficiary;
        this.operationDate = operationDate;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }

    public Transaction(int accNumber, String beneficiary, String operationDate, double amount, String currency, String comment) {
        this.accNumber = accNumber;
        this.beneficiary = beneficiary;
        this.operationDate = operationDate;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(int accNumber) throws InvalidValueProvided {
        if (accNumber > 0) {
            this.accNumber = accNumber;
        } else
            throw new InvalidValueProvided("Invalid account number value");

    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) throws InvalidValueProvided {
        if (!beneficiary.trim().isEmpty() ) {
            this.beneficiary = beneficiary;
        } else
            throw new InvalidValueProvided("Invalid beneficiary value");

    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) throws InvalidValueProvided {
        if (!operationDate.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(operationDate);
                this.operationDate = operationDate;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InvalidValueProvided("Invalid date format or value");
            }
        } else
            throw new InvalidValueProvided("Invalid date value or not provided");
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws InvalidValueProvided {
        if (amount > 0) {
            this.amount = amount;
        }else
        throw new InvalidValueProvided("Invalid amount value");
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) throws InvalidValueProvided {
        if (!currency.trim().isEmpty()) {
            this.currency = currency;
        }else
        throw new InvalidValueProvided("Invalid currency value");
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if(comment.length() > 20) {
            this.comment = comment.substring(0, 20);
        }else this.comment = comment;
    }

}
