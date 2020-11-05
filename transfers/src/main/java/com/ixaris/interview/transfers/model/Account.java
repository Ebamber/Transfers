package com.ixaris.interview.transfers.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    //I'm using Strings for identifiers as I won't be using them for and calculations and the strings
    // offer me more flexibility

    private String accountId;
    private Double balance;
    private Integer usagesAsSource;

}
