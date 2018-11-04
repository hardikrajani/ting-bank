package com.hardik.bank.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hardik.bank.common.model.BankTransaction;

public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

}
