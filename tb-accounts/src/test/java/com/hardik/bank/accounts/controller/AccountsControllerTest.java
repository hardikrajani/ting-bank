package com.hardik.bank.accounts.controller;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;


@SpringBootTest
public class AccountsControllerTest {

	private AccountsController target;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionServiceClient transactionServiceClient;

    @Before
    public void init() {
        accountRepository = Mockito.mock(AccountRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        transactionServiceClient = Mockito.mock(TransactionServiceClient.class);
        target = new AccountsController(accountRepository, customerRepository, transactionServiceClient);
    }

    @Test
    public void testOpenAccount() {
        Mockito.when(accountRepository.save(any(Account.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(new Customer()));

        Account accountCreated = target.openAccount(new OpenAccountRequest(1, 0d));
        assertNotNull(accountCreated);
        assertNotNull(accountCreated.getBankTransactions());
        assertEquals(0, accountCreated.getBankTransactions().size());
    }

    @Test
    public void testOpenAccountWithInitialCredit() {
        Mockito.when(accountRepository.save(any(Account.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(accountRepository.saveAndFlush(any(Account.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(new Customer()));
        Mockito.when(transactionServiceClient.makeTransaction(any(TransactionRequest.class))).thenReturn(new BankTransaction(null,5d, TransactionType.CREDIT));

        Account accountCreated = target.openAccount(new OpenAccountRequest(1, 5d));
        assertNotNull(accountCreated);
        assertNotNull(accountCreated.getBankTransactions());
        assertEquals(5, accountCreated.getBalance(), 0.1); // delta is set to 0.1 to remove uncertainty double values
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testOpenAccountCustomerNotFound() {
        Mockito.when(customerRepository.findById(1l)).thenReturn(Optional.empty());

        target.openAccount(new OpenAccountRequest(1, 0d));
    }

    @Test(expected = BadInputException.class)
    public void testOpenAccountNegativeInitialCredit() {
        Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(new Customer()));

        target.openAccount(new OpenAccountRequest(1, -1.0d));
    }

    @Test
    public void testGetAccountById() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));

        Account account = target.getAccountById(1);
        assertNotNull(account);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetAccountByIdNoAccount() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.empty());

        Account account = target.getAccountById(1);
        assertNotNull(account);
    }

    @Test
    public void testGetAccountByCustomerId() {
        Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer(1, "Test")));

        List<Account> accountList = target.getAccountsByCustomerId(1);
        assertNotNull(accountList);
    }
}
