package com.example.csv_acc_balance_manager.controller;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.service.CSVService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.ParseException;
import java.util.Objects;

@Controller
@RequestMapping("/csv")
public class CSVController {

    private final CSVService csvService;
    private final static String FILE_NAME = "transactions.csv";
    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/postCSV")
    public ResponseEntity<String> saveTransactionsToDB (@RequestParam("csvFile") MultipartFile csvFile){

        try {
            csvService.saveCSVTransactionsIntoDB(csvFile);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to convert and save CSV file to DB. Invalid file type or values.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("CSV converted and saved successfully", HttpStatus.OK);
    }

    @GetMapping("/getCSV")
    public ResponseEntity<Resource> getTransactionsFromDBasCSVFile(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate){
        InputStreamResource file = null;
        try {
            ByteArrayInputStream byteArrayInputStream = csvService.getCSVFile(fromDate, toDate);
            file = new InputStreamResource(byteArrayInputStream);
        } catch (InvalidValueProvided | ParseException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition.attachment().filename(Objects.requireNonNull(FILE_NAME)).build();
        headers.setContentDisposition(disposition);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
}
