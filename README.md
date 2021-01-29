This project was created for the purpose of meeting the requirements for WCF-MarkP:

- Read Balances.csv (located on the resource directory)
- Create Summary.csv
- Created on a MAC with IntelliJ Community, Java 8, SpringBoot, and PostgreSQL
- Implements Command Line and JDBC
- See application.properties for more information.


I am running a postgreSQL server on my localhost.  I created a database mydb.
User danielb was created as a superuser.

The PostgreSQL table used to store and summarize the data.

create table balances
(
	balances_id serial,
	first_name varchar(50),
	last_name varchar(50),
	address varchar(100),
	city varchar(50),
	state varchar(50),
	zip varchar(50),
	phone varchar(50),
	balance numeric(20,2),
	createtime timestamp not null,
	filename varchar(100)
);

create index createfileidx on balances 
(createtime, filename);


