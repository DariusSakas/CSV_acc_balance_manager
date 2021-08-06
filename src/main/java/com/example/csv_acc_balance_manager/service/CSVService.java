package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.example.csv_acc_balance_manager.util.TransactionsListParser;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CSVService {

    //Headers format. First line of new .csv file
    private static final CSVFormat format = CSVFormat.DEFAULT.withHeader(
            "Account", "Date", "Beneficiary", "Comment", "Amount", "Currency");

    private final TransactionRepository transactionRepository;
    private final TransactionsListParser transactionsListParser;

    public CSVService(TransactionRepository transactionRepository, TransactionsListParser transactionsListParser) {
        this.transactionRepository = transactionRepository;
        this.transactionsListParser = transactionsListParser;
    }

    //Convert Multipart csvFile into List<Transaction> and save each element to DB
    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) throws Exception {
        List<Transaction> transactions = transactionsListParser.formatCSVFileIntoCSVModelList(csvFile);
        transactionRepository.saveAll(transactions);
    }

    /*
    ByteArrayInputStream contains an internal buffer that contains bytes that may be read from the stream.
    Will check dataFrom and dateTo validation. If dates format valid continue, else exception.
    transactionList data using PrintWriter is converted into bytes with defined format.
    CSVPrinter prints all data from list into outputStream as bytes using .printTransactionsToCSV() method.
     */
    public ByteArrayInputStream getCSVFile(String dateFrom, String dateTo) throws InvalidValueProvided, ParseException {

        String dateFromValid = transactionsListParser.getValidDateFormatElseEmpty(dateFrom);
        String dateToValid = transactionsListParser.getValidDateFormatElseEmpty(dateTo);

        List<Transaction> transactionList = transactionsListParser.getTransactionsList(dateFromValid, dateToValid);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format);
            printTransactionsToCSV(transactionList, csvPrinter);

            //Flushes the output stream and forces any buffered output bytes to be written out.
            csvPrinter.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import data to CSV from DB");
        }
    }

    //.printRecord() takes each value of Transaction object and writes it as one array.
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


}
