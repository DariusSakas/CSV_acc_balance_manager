package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CSVService {

    private static final CSVFormat format = CSVFormat.DEFAULT.withHeader("Account", "Date", "Beneficiary", "Comment", "Amount", "Currency");
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final String CSV_FILE_NAME = "./transactions.csv";

    private final TransactionRepository transactionRepository;

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

        parseCSVIntoTransactionList(transactionList, csvRecords, isIterationSuccessful);

        if (isIterationSuccessful.get())
            return transactionList;
        else
            throw new InvalidValueProvided("Invalid value provided. Check CSV files values.");
    }

    private void parseCSVIntoTransactionList(List<Transaction> transactionList, Iterable<CSVRecord> csvRecords, AtomicBoolean isIterationSuccessful) {
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

    public ByteArrayInputStream getCSVFile(String dateFrom, String dateTo) throws InvalidValueProvided, ParseException {

        String dateFromValid = getValidDateFormatElseNull(dateFrom);
        String dateToValid = getValidDateFormatElseNull(dateTo);

        List<Transaction> transactionList = getTransactionsList(dateFromValid, dateToValid);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format);

            printTransactionsToCSV(transactionList, csvPrinter);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void printTransactionsToCSV(List<Transaction> transactionList, CSVPrinter csvPrinter) {
        transactionList.forEach(t ->
        {
            try {
                csvPrinter.printRecord(Arrays.asList(
                        String.valueOf(t.getAccNumber()),
                        t.getOperationDate(),
                        t.getBeneficiary(),
                        t.getComment(),
                        t.getAmount(),
                        t.getCurrency()
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private List<Transaction> getTransactionsList(String dateFromValid, String dateToValid) {
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

    private List<Transaction> getTransactionsListValidTo(String dateToValid, List<Transaction> transactions) {
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

    private List<Transaction> getTransactionsListValidFromTo(String dateFromValid, String dateToValid, List<Transaction> transactions) {
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

    private List<Transaction> getTransactionsListValidFrom(String dateFromValid, List<Transaction> transactions) {
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
