package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CSVService {

    private final TransactionRepository transactionRepository;
    private final String CSV_FILE_NAME = "./transactions.csv";

    public CSVService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) throws Exception {
        List<Transaction> transactions = formatCSVFileIntoCSVModelList(csvFile);
        transactionRepository.saveAll(transactions);
    }

    private List<Transaction> formatCSVFileIntoCSVModelList(MultipartFile csvFile) throws Exception {
        List<Transaction> transactionList = new ArrayList<>();

        BufferedReader inputFile = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(inputFile, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        AtomicBoolean isIterationSuccessful = new AtomicBoolean(false);

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

        if (isIterationSuccessful.get())
            return transactionList;
        else
            throw new InvalidValueProvided("Invalid value provided. Check CSV files values.");
    }
    public CSVPrinter getCSVFile(String dateFrom, String dateTo) throws InvalidValueProvided, ParseException {
        String dateFromValid = getValidDateFormatElseNull(dateFrom);
        String dateToValid = getValidDateFormatElseNull(dateTo);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> sortedTransactionsList = new ArrayList<>();

        if(dateFromValid != null && dateToValid == null){
            sortedTransactionsList = getTransactionsListValidFrom(dateFromValid, dateFormat, transactions);
        }else if(dateFromValid != null && dateToValid != null){
            sortedTransactionsList = getTransactionsListValidFromTo(dateFromValid, dateToValid, dateFormat, transactions);
        }

        return null;
    }

    private List<Transaction> getTransactionsListValidFromTo(String dateFromValid, String dateToValid, DateFormat dateFormat, List<Transaction> transactions) {
        return transactions.stream().filter(t->
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

    private List<Transaction> getTransactionsListValidFrom(String dateFromValid, DateFormat dateFormat, List<Transaction> transactions) {
        return transactions.stream().filter(t->
        {
            try {
                return dateFormat.parse(t.getOperationDate()).after(dateFormat.parse(dateFromValid));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    public String getValidDateFormatElseNull(String dateToCheck) throws InvalidValueProvided, ParseException {
        if (!dateToCheck.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateToCheck);
                return dateToCheck;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InvalidValueProvided("Invalid date format or value");
            }
        } else if (dateToCheck.isEmpty()) {
            return null;
        }
        return null;
    }
}
