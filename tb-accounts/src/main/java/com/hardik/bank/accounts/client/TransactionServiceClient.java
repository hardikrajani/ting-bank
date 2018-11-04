package com.hardik.bank.accounts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hardik.bank.common.model.BankTransaction;
import com.hardik.bank.common.wrapper.TransactionRequest;

@FeignClient(name="transaction-service", url="${service.transaction.url}")
public interface TransactionServiceClient {
    @PostMapping
    BankTransaction makeTransaction(@RequestBody TransactionRequest transactionRequest);

}
