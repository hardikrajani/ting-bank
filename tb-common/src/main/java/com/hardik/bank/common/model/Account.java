package com.hardik.bank.common.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Account {

    long accountId;
    double balance;
    Customer customer;
    List<BankTransaction> bankTransactions = new ArrayList<>();

    public Account() {

    }

    public Account(Customer customer, double balance) {
        this.customer = customer;
        this.balance = balance;
    }

    public void updateBalance(double amount) {
        this.balance+= amount;
    }
}
