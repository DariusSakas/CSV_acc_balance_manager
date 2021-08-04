package com.example.csv_acc_balance_manager.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "accNumber", "operationDate", "beneficiary",
        "comment", "amount", "currency" })
public class CSVModel {

    private Long transactionId;
    private int accNumber;
    private String operationDate;
    private String beneficiary;
    private double amount;
    private String currency;
    private String comment;

    public CSVModel(Long transactionId, int accNumber, String operationDate, String beneficiary, double amount, String currency, String comment) {

        this.transactionId = transactionId;
        this.accNumber = accNumber;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
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

    public void setAccNumber(int accNumber) {
        this.accNumber = accNumber;
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
}

