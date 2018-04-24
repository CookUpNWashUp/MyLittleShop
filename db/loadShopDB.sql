CREATE TABLE products(
	product_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
	name VARCHAR(30) NOT NULL,
	unit VARCHAR(12) NOT NULL,
	price INTEGER NOT NULL	
);

/*The table specifies the incoming and outgoing products at shop 1
	the sum of quantity of imports minus the sum of quantity of exports equals the available inventory
	This information is to be returned by a query on client side*/

CREATE TABLE Shop01Log(
	log_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	product_ID INTEGER,
	isImport BOOLEAN NOT NULL,
	quantity INTEGER NOT NULL,
	import_time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	export_time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	FOREIGN KEY(product_ID) REFERENCES products(product_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE Shop02Log(
	log_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	product_ID INTEGER,
	isImport BOOLEAN NOT NULL,
	quantity INTEGER NOT NULL,
	import_time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	export_time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',
	FOREIGN KEY(product_ID) REFERENCES products(product_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);
	