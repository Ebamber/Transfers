package com.ixaris.interview.transfers.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Transaction {
    //I'm using Strings for identifiers as I won't be using them for and calculations and the strings
    // offer me more flexibility

    private String sourceAccount;
    private String destinationAccount;
    private Double amount;
    private Date date;
    private String transactionId;

}
