# Ting Bank

### Description

Ting Bank is an application aimed to show how we can write microservices using spring boot.

The application has 3 maven modules representing following.

1. Common - Provides basic models shared between different microservices
2. Accounts - Provides APIs for Account related operations like Open Bank Account
3. Transactions - Provides APIs for transactions like deposit, withdraw

I have used **Java 8, Spring Boot, Swagger** (to test the APIs) and **H2 database**. 

Please note that H2 db is not embedded as accounts and transactions both use the same database with their own session factory. This should be improved for better performance.

### How to run the app

Following are the steps to run the application

1. Start H2 server
```
cd <PROJECT_HOME>/db-server
java -cp h2*.jar org.h2.tools.Server -webAllowOthers -tcpAllowOthers
```
2. Run Maven - Build all components first
```
cd <PROJECT_HOME>s
mvn clean install
```
3. Start tb-accounts module
```
cd <PROJECT_HOME>/tb-accounts
mvn spring-boot:run
```
4. Start tb-transaction module
```
cd <PROJECT_HOME>/tb-transactions
mvn spring-boot:run
```
5. Use following links to access different APIs or consolve

* [Accounts](http://localhost:18081/swagger-ui.html)
* [Transactios](http://localhost:18082/swagger-ui.html)
* [H2 Database Console](http://localhost:8082/console/)

### Assumptions

1. Customer can create as many accounts as he wants
2. DB Performance & Security is considered out of scope

### Extending the example

I can stil add following 

1. Liferay CMS - Portlet based UI to allow end user to access the APIs
2. Docker - For containerization
3. Better transaction management