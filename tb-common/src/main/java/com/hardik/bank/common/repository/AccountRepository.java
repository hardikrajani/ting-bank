package com.hardik.bank.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hardik.bank.common.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId")
    List<Account> findByCustomerId(long customerId);
}
