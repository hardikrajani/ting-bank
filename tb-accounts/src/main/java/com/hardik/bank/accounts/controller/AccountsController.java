package com.hardik.bank.accounts.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.bank.accounts.client.TransactionServiceClient;
import com.hardik.bank.common.enums.TransactionType;
import com.hardik.bank.common.exception.AccountNotFoundException;
import com.hardik.bank.common.exception.BadInputException;
import com.hardik.bank.common.exception.CustomerNotFoundException;
import com.hardik.bank.common.model.Account;
import com.hardik.bank.common.model.BankTransaction;
import com.hardik.bank.common.model.Customer;
import com.hardik.bank.common.repository.AccountRepository;
import com.hardik.bank.common.repository.CustomerRepository;
import com.hardik.bank.common.wrapper.OpenAccountRequest;
import com.hardik.bank.common.wrapper.TransactionRequest;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionServiceClient transactionServiceClient;


    public AccountsController() {

    }

    public AccountsController(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionServiceClient transactionServiceClient) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionServiceClient = transactionServiceClient;
    }


    @GetMapping(path = "/{accountId}")
    public Account getAccountById(@PathVariable("accountId") final long accountId) {
        LOG.info("getAccountById: [{}]", accountId);
        if(accountId < 0) {
            throw new BadInputException("AccountId can not be negative.");
        }
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if(accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new AccountNotFoundException("Account not found");
        }


    }

    @PostMapping
    public Account openAccount(@RequestBody OpenAccountRequest request) {
        LOG.info("createAccount: [{}]", request);

        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        if(!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer not found.");
        }

        if(request.getInitialCredit() < 0) {
            throw new BadInputException("initialCredit can not be negative.");
        }
        Account account = accountRepository.save(new Account(customer.get(), BigDecimal.ZERO.doubleValue()));
        accountRepository.saveAndFlush(account);

        if(request.getInitialCredit() > BigDecimal.ZERO.doubleValue()) {
            BankTransaction bankTransaction = transactionServiceClient.makeTransaction(new TransactionRequest(account.getAccountId(), request.getInitialCredit(), TransactionType.CREDIT));
            account.updateBalance(bankTransaction.getAmount());
            account = accountRepository.saveAndFlush(account);
            account.getBankTransactions().add(bankTransaction);
        }
        return account;
    }

    @GetMapping(path = "action/findAccountsByCustomer/{customerId}")
    public List<Account> getAccountsByCustomerId(@PathVariable("customerId") final long customerId) {
        LOG.info("getAccountByCustomerId: [{}]", customerId);

        Optional<Customer> customer = customerRepository.findById(customerId);
        if(!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer not found.");
        }

        return accountRepository.findByCustomerId(customerId);
    }

}
