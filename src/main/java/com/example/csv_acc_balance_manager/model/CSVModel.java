package com.example.csv_acc_balance_manager.model;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class CSVModel {

    private int account;
    private String operationDate;
    private String beneficiary;
    private double amount;
    private String currency;
    private String comment;

    public CSVModel( int account, String operationDate, String beneficiary, double amount, String currency, String comment) {

        this.account = account;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;

    }

    public CSVModel() {
    }

    public int getAccNumber()  {
        return account;
    }

    public void setAccNumber(int accNumber) {
        this.account = accNumber;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CSVModel{" +
                "account=" + account +
                ", operationDate='" + operationDate + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}

