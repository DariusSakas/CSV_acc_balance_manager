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
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/*
Class holds all methods to parse all .csv file data into List<Transaction>
and fetch data from DB to create new .csv file at CSVController controller class.
 */
@Component
public class TransactionsListParser {

    //Data format will used to check if valid data format used in .csv file transaction
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final TransactionRepository transactionRepository;

    public TransactionsListParser(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /*
    InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset
    BufferedReader reads text from a character-input stream, buffering characters to provide for the efficient reading of characters, arrays, and lines.
    Parses CSV files according to the specified format.

    .parseCSVIntoTransactionList() method used to iterate through records and collect data from each record,
    set it to Transaction object using Transaction setters and add each element to the list,
    that will be returned by .formatCSVFileIntoCSVModelList() method.
     */
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

    /*
    Iterates through csvRecords and add each record data to the Transaction object using setters.
    Setters have validation (check Transaction.class). If validation fails isIterationSuccessful boolean value
    false is returned by this method and InvalidValueProvided exception thrown at .formatCSVFileIntoCSVModelList()
    method. Else all data is set successfully.
     */
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

    //Fetch data from DB. Check if dateFromValid or dateToValid has any data. According to that method is called.
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

    //Returns List<Transaction>
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

    //If data is empty or null, empty String returned. If data exist, it is checked by trying to parse it.
    public String getValidDateFormatElseEmpty(String dateToCheck) throws InvalidValueProvided {
        if (dateToCheck != null && !dateToCheck.trim().isEmpty()) {
            try {
                Date date = dateFormat.parse(dateToCheck);
                return dateToCheck;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InvalidValueProvided("Invalid date format or value");
            }
        } else if (dateToCheck == null || dateToCheck.trim().isEmpty()) {
            return "";
        }
        return "";
    }
}
