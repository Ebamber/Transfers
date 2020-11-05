package com.ixaris.interview.transfers.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileReaderResponse {

    private List<String> accountBalances;
    private Account mostUsedSource;
    private Account highestBalanceAccount;

}
