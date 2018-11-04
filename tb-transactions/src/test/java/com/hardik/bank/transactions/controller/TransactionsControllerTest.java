package com.hardik.bank.transactions.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.hardik.bank.common.enums.TransactionType;
import com.hardik.bank.common.exception.AccountNotFoundException;
import com.hardik.bank.common.exception.BadInputException;
import com.hardik.bank.common.model.Account;
import com.hardik.bank.common.model.BankTransaction;
import com.hardik.bank.common.repository.AccountRepository;
import com.hardik.bank.common.repository.TransactionRepository;
import com.hardik.bank.common.wrapper.TransactionRequest;

@SpringBootTest
public class TransactionsControllerTest {

    private TransactionsController target;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Before
    public void init() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);
        target = new TransactionsController(accountRepository, transactionRepository);
    }

    @Test
    public void testMakeTransaction() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(null, 1)));
        Mockito.when(transactionRepository.save(any(BankTransaction.class))).then(AdditionalAnswers.returnsFirstArg());

        BankTransaction transaction = target.makeTransaction(new TransactionRequest(1, 10d, TransactionType.CREDIT));
        assertNotNull(transaction);
        assertNotNull(transaction.getType());
        assertEquals(TransactionType.CREDIT, transaction.getType());
    }

    @Test
    public void testDeposit() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(null, 1)));
        Mockito.when(transactionRepository.save(any(BankTransaction.class))).then(AdditionalAnswers.returnsFirstArg());

        BankTransaction transaction = target.deposit(1, 10d);
        assertNotNull(transaction);
        assertNotNull(transaction.getType());
        assertEquals(TransactionType.CREDIT, transaction.getType());
    }

    @Test
    public void testWithdraw() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(null, 10)));
        Mockito.when(transactionRepository.save(any(BankTransaction.class))).then(AdditionalAnswers.returnsFirstArg());

        BankTransaction transaction = target.withdraw(1, 1d);
        assertNotNull(transaction);
        assertNotNull(transaction.getType());
        assertEquals(TransactionType.DEBIT, transaction.getType());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testOpenAccountCustomerNotFound() {
        Mockito.when(accountRepository.findById(1l)).thenReturn(Optional.empty());

        target.withdraw(1, 10d);
    }

    @Test(expected = BadInputException.class)
    public void testOpenAccountNegativeAmount() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(null, 10.0)));

        target.deposit(1, -11.0d);
    }

    @Test(expected = BadInputException.class)
    public void testOpenAccountInsufficientBalance() {
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(null, 10.0)));

        target.withdraw(1, 11.0d);
    }
}
