package com.example.csv_acc_balance_manager.model;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;

import javax.persistence.*;
import java.text.DateFormat;
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
    private double finalBalance;

    public Transaction() {
    }

    public Transaction(Long transactionId, int accNumber, String beneficiary, String operationDate, double amount, String currency, String comment, double finalBalance) {
        this.transactionId = transactionId;
        this.accNumber = accNumber;
        this.beneficiary = beneficiary;
        this.operationDate = operationDate;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.finalBalance = finalBalance;
    }

    public Transaction(int accNumber, String beneficiary, String operationDate, double amount, String currency, String comment, double finalBalance) {
        this.accNumber = accNumber;
        this.beneficiary = beneficiary;
        this.operationDate = operationDate;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.finalBalance = finalBalance;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(int accNumber) throws InvalidValueProvided {
        if(accNumber > 0) {
            this.accNumber = accNumber;
        }
        throw new InvalidValueProvided("Invalid account number value");
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) throws InvalidValueProvided {
        if (!beneficiary.isEmpty()){
            this.beneficiary = beneficiary;
        }
        throw new InvalidValueProvided("Invalid beneficiary value");

    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) throws InvalidValueProvided {
        if(!operationDate.isEmpty()) {
            try{
                Date date =new SimpleDateFormat("dd/MM/yyyy").parse(operationDate);
                this.operationDate = operationDate;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InvalidValueProvided("Invalid date value");
            }
        }
        throw new InvalidValueProvided("Invalid date value");
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws InvalidValueProvided {
        if(amount > 0) {
            this.amount = amount;
        }
        throw new InvalidValueProvided("Invalid amount value");
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) throws InvalidValueProvided {
        if(!currency.isEmpty()) {
            this.currency = currency;
        }
        throw new InvalidValueProvided("Invalid amount value");
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }
}
