package com.example.csv_acc_balance_manager.model;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private final Transaction transaction = new Transaction();

    @Test
    void setAccNumber() {
        try {
            transaction.setAccNumber(2);
        } catch (InvalidValueProvided e) {
            e.printStackTrace();
        }
        assertEquals(2, transaction.getAccNumber());

        Throwable exceptionValue = assertThrows(InvalidValueProvided.class, () -> transaction.setAccNumber(-1));
        assertEquals("Invalid account number value", exceptionValue.getMessage());

        Throwable exceptionValueIsEmpty = assertThrows(NumberFormatException.class, () -> transaction.setAccNumber(Integer.parseInt("")));
        assertEquals(NumberFormatException.class, exceptionValueIsEmpty.getClass());
    }

    @Test
    void setBeneficiary() {
        try {
            transaction.setBeneficiary("test");
        } catch (InvalidValueProvided e) {
            e.printStackTrace();
        }
        assertEquals("test", transaction.getBeneficiary());

        Throwable exceptionValue = assertThrows(InvalidValueProvided.class, () -> transaction.setBeneficiary(""));
        assertEquals("Invalid beneficiary value", exceptionValue.getMessage());

        Throwable exceptionValueWhenSpaces = assertThrows(InvalidValueProvided.class, () -> transaction.setBeneficiary("   "));
        assertEquals("Invalid beneficiary value", exceptionValue.getMessage());
    }

    @Test
    void setOperationDate() {
        try {
            transaction.setOperationDate("11/11/1111");
        } catch (InvalidValueProvided e) {
            e.printStackTrace();
        }
        assertEquals("11/11/1111", transaction.getOperationDate());

        Throwable exceptionValue = assertThrows(InvalidValueProvided.class, () -> transaction.setOperationDate("11.11.1111"));
        assertEquals("Invalid date format or value", exceptionValue.getMessage());

        Throwable exceptionValueIsEmpty = assertThrows(InvalidValueProvided.class, () -> transaction.setOperationDate(""));
        assertEquals("Invalid date value or not provided", exceptionValueIsEmpty.getMessage());
    }

    @Test
    void setAmount() {
        try {
            transaction.setAmount(2.212);
        } catch (InvalidValueProvided e) {
            e.printStackTrace();
        }
        assertEquals(2.212, transaction.getAmount());

        Throwable exceptionValue = assertThrows(InvalidValueProvided.class, () -> transaction.setAmount(-1));
        assertEquals("Invalid amount value", exceptionValue.getMessage());

        Throwable exceptionValueIsEmpty = assertThrows(NumberFormatException.class, () -> transaction.setAmount(Double.parseDouble("")));
        assertEquals(NumberFormatException.class, exceptionValueIsEmpty.getClass());

    }

    @Test
    void setCurrency() {
        try {
            transaction.setCurrency("EU");
        } catch (InvalidValueProvided e) {
            e.printStackTrace();
        }
        assertEquals("EU", transaction.getCurrency());

        Throwable exceptionValueIsEmpty = assertThrows(InvalidValueProvided.class, () -> transaction.setCurrency(""));
        assertEquals("Invalid currency value", exceptionValueIsEmpty.getMessage());
    }

    @Test
    void setComment() {
        String veryLongComment = "verylongcomment2231kdk21d12312312d12d21";
        transaction.setComment(veryLongComment);
        assertEquals(veryLongComment.substring(0, 20), transaction.getComment());
    }
}
