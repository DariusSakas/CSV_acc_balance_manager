package com.example.csv_acc_balance_manager.util;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class TransactionsListParser {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final TransactionRepository transactionRepository;

    public TransactionsListParser(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> formatCSVFileIntoCSVModelList(MultipartFile csvFile) throws Exception {
        List<Transaction> transactionList = new ArrayList<>();

        BufferedReader inputFile = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(inputFile, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        AtomicBoolean isIterationSuccessful = new AtomicBoolean(false);

        parseCSVIntoTransactionList(transactionList, csvRecords, isIterationSuccessful);

        if (isIterationSuccessful.get())
            return transactionList;
        else
            throw new InvalidValueProvided("Invalid value provided. Check CSV files values.");
    }

    public void parseCSVIntoTransactionList(List<Transaction> transactionList, Iterable<CSVRecord> csvRecords, AtomicBoolean isIterationSuccessful) {
        csvRecords.forEach(csvRecord -> {
                    try {
                        isIterationSuccessful.set(false);

                        Transaction transaction = new Transaction();
                        transaction.setAccNumber(Integer.parseInt(csvRecord.get(0)));
                        transaction.setOperationDate(csvRecord.get(1));
                        transaction.setBeneficiary(csvRecord.get(2));
                        transaction.setComment(csvRecord.get(3));
                        transaction.setAmount(Double.parseDouble(csvRecord.get(4)));
                        transaction.setCurrency(csvRecord.get(5));
                        transactionList.add(transaction);

                        isIterationSuccessful.set(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public List<Transaction> getTransactionsList(String dateFromValid, String dateToValid) {
        List<Transaction> transactions = transactionRepository.findAll();

        if (!dateFromValid.isEmpty() && dateToValid.isEmpty()) {
            return getTransactionsListValidFrom(dateFromValid, transactions);
        } else if (!dateFromValid.isEmpty() && !dateToValid.isEmpty()) {
            return getTransactionsListValidFromTo(dateFromValid, dateToValid, transactions);
        } else if (dateFromValid.isEmpty() && !dateToValid.isEmpty()) {
            return getTransactionsListValidTo(dateToValid, transactions);
        } else
            return transactions;
    }

    public List<Transaction> getTransactionsListValidTo(String dateToValid, List<Transaction> transactions) {
        return transactions.stream().filter(t ->
        {
            try {
                return dateFormat.parse(t.getOperationDate()).before(dateFormat.parse(dateToValid));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsListValidFromTo(String dateFromValid, String dateToValid, List<Transaction> transactions) {
        return transactions.stream().filter(t ->
        {
            try {
                return dateFormat.parse(t.getOperationDate()).after(dateFormat.parse(dateFromValid)) &&
                        dateFormat.parse(t.getOperationDate()).before(dateFormat.parse(dateToValid));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsListValidFrom(String dateFromValid, List<Transaction> transactions) {
        return transactions.stream().filter(t ->
        {
            try {
                return dateFormat.parse(t.getOperationDate()).after(dateFormat.parse(dateFromValid));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }
}
