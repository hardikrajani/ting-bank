package com.hardik.bank.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hardik.bank.common.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
