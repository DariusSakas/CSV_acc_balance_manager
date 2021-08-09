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

/*
    Controller holds all API points to post and upload .csv file
     */

@Controller
@RequestMapping("/csv")
public class CSVController {

    private final CSVService csvService;

    //Final file name for download
    private final static String FILE_NAME = "transactions.csv";

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    // Post .csv file API. Use multi-part data format. Asks for param "csvFile".
    @PostMapping("/postCSV")
    public ResponseEntity<String> saveTransactionsToDB(@RequestParam("csvFile") MultipartFile csvFile) {

        try {
            csvService.saveCSVTransactionsIntoDB(csvFile);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to convert and save CSV file to DB. Invalid file type or values.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("CSV converted and saved successfully", HttpStatus.OK);
    }

    /*
    Fetch Transaction data from DB, convert it to .csv file and return for client.
    Params fromDate and toDate are optional. If not provided all Transactions will be converted to .csv file.
    Else Transactions that are from or up to those dates will provided.
     */
    @GetMapping("/getCSV")
    public ResponseEntity<?> getTransactionsFromDBasCSVFile(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate) {
        try {


            // 1.Get file byte array using .getCSVFile() method.
            ByteArrayInputStream byteArrayInputStream = csvService.getCSVFile(fromDate, toDate);
            // 2. Convert byte array into file.
            InputStreamResource file = new InputStreamResource(byteArrayInputStream);
            // 3.Convert resource into media type defined in the HTTP specification.
            MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM);
            // 4. Http request data structure.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            // 5. .attachment() - init browser download, .inline() - init open in browser
            ContentDisposition disposition = ContentDisposition.attachment().filename(Objects.requireNonNull(FILE_NAME)).build();
            // 6. Header holds all multipart body data in http context
            headers.setContentDisposition(disposition);

            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        } catch (InvalidValueProvided | ParseException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Unable to parse transactions from DB to .csv file. Check data value for dd/MM/yyyy", HttpStatus.OK);
    }

}
