 CREATE TABLE products(
	product_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
	name VARCHAR(30) NOT NULL,
	unit VARCHAR(12) NOT NULL,
	price INTEGER NOT NULL,
	time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00'
);

/*The table specifies the incoming and outgoing products at shop 1
	the sum of quantity of imports minus the sum of quantity of exports equals the available inventory
	This information is to be returned by a query on client side*/

CREATE TABLE Shop01Log(
	log_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	product_ID INTEGER,
	isImport BOOLEAN NOT NULL,
	quantity INTEGER NOT NULL,
	time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	FOREIGN KEY(product_ID) REFERENCES products(product_ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE Shop02Log(
	log_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	product_ID INTEGER,
	IsImport BOOLEAN NOT NULL,
	quantity INTEGER NOT NULL,
	time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	FOREIGN KEY(product_ID) REFERENCES products(product_ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE userinformation(
        username VARCHAR(32) PRIMARY KEY NOT NULL,
        password VARCHAR(39) NOT NULL,
        domain VARCHAR(5) NOT NULL
);
	
INSERT INTO userinformation(username,password,domain) values('manager','123','01');