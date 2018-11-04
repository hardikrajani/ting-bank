package com.hardik.bank.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hardik.bank.common.enums.TransactionType;

import lombok.Data;

@Data
public class BankTransaction {

    long transactionId;

    double amount;

    @JsonIgnore
    Account account;

    TransactionType type;

    public BankTransaction() {

    }

    public BankTransaction(Account account, double amount, TransactionType type) {
        this.amount = amount;
        this.account = account;
        this.type = type;
    }
}
