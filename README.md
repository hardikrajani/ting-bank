# Ting Bank

### Description

Ting Bank kind of sub application. It allows existing users of the application to open an account. I.E. Amazon started amazon pay, they created wallet account for their existing users. 

This project provides API to open account and transact for the account. API provides functionality like open account, list accounts, make credit/withdraw transaction, list transactions.

There are three entities included in API.
1) Customer
2) Accounts
3) Transactions

### How to run the app

Following are the steps to run the application
1) Start H2 server
	Go to dbserver/ directory.
	run following command.
	java -cp h2*.jar org.h2.tools.Server -webAllowOthers -tcpAllowOthers
2) Start tb-accounts module
	Go to tb-accounts/ directory
	run spring boot application
3) Start tb-transaction module
	Go to tb-transactions/ directory
	run spring boot application
4) Swagger is configured for both the module. Link details are provided in Links section.

### Assumptions

1. Security is considered out of scope for this application
2. All the amounts are in double to keep API simple
3. Customer can create as many accounts as he wants

> Maven
```bash
mvn spring-boot:run
```

### Links

* [Accounts](http://localhost:18081/swagger-ui.html)
* [Transactios](http://localhost:18082/swagger-ui.html)
* [H2 Database Console](http://localhost:8080/console/)

### Tools used

1. Java 8
2. Spring Boot 2
3. H2 server
4. Swagger
