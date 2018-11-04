package com.hardik.bank.transactions.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bank.common.enums.TransactionType;
import com.hardik.bank.common.exception.AccountNotFoundException;
import com.hardik.bank.common.exception.BadInputException;
import com.hardik.bank.common.model.Account;
import com.hardik.bank.common.model.BankTransaction;
import com.hardik.bank.common.repository.AccountRepository;
import com.hardik.bank.common.repository.TransactionRepository;
import com.hardik.bank.common.wrapper.TransactionRequest;

@RestController
@RequestMapping("/bankTransactions")
public class TransactionsController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionsController() {

    }

    public TransactionsController(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    public BankTransaction makeTransaction(@RequestBody TransactionRequest transactionRequest) {
        LOG.info("makeTransaction: [{}], amount [{}], transactionType [{}]", transactionRequest.getAccountId(), transactionRequest.getAmount(), transactionRequest.getTransactionType());

        return doTransaction(transactionRequest.getAccountId(), transactionRequest.getAmount(), transactionRequest.getTransactionType(), Boolean.FALSE);
    }

    @PutMapping("action/deposit/{accountId}/amount/{amount}")
    public BankTransaction deposit(@PathVariable("accountId") long accountId, @PathVariable("amount") double amount) {
        LOG.info("deposit: [{}], amount [{}]", accountId, amount);

        return doTransaction(accountId, amount, TransactionType.CREDIT);
    }

    @PutMapping("action/withdraw/{accountId}/amount/{amount}")
    public BankTransaction withdraw(@PathVariable("accountId") long accountId, @PathVariable("amount") double amount) {
        LOG.info("withdraw: [{}], amount [{}]", accountId, amount);

        return doTransaction(accountId, amount, TransactionType.DEBIT);
    }

    private BankTransaction doTransaction(long accountId, double amount, TransactionType transactionType) {
        return doTransaction(accountId, amount, transactionType, Boolean.TRUE);
    }

    private BankTransaction doTransaction(long accountId, double amount, TransactionType transactionType, boolean updateBalance) {
        if(amount <= 0) {
            throw new BadInputException("Amount is not valid.");
        }

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent()) {
            throw new AccountNotFoundException("Account Not Found");
        }

        Account account = accountOptional.get();

        if(transactionType.equals(TransactionType.DEBIT) && amount > account.getBalance()) {
            throw new BadInputException("Amount is more than balance.");
        }

        BankTransaction bankTransaction = new BankTransaction(account, amount,transactionType);
        bankTransaction = transactionRepository.save(bankTransaction);

        if(updateBalance) {
            account.updateBalance(amount * (transactionType.equals(TransactionType.DEBIT) ? -1 : 1));
            accountRepository.saveAndFlush(account);
        }

        return bankTransaction;
    }
}
