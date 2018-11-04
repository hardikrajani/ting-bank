package com.hardik.bank.common.wrapper;

import com.hardik.bank.common.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

	long accountId;
	double amount;
	TransactionType transactionType;
}
