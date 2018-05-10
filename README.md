# MyLittleShop
## Change Log
- v1.0 (master): Added database connection. Database initialization scripts (products, demo shop logs).API list added: getproduct, log ,import, export, q shutdown hook. Console client app added and seperated into a different package.

- v1.1: APIs:  getallproducts, getinventory, addproduct, addshop, deleteshop, delete product. Shop logs' structure changed (Have import time/ export time removed and replaced with a single time column)

- v1.1.1: SSL sockets implemented. Exception handling. Hard hashing for user's passwords implemted. createUser API created but not yet implemented

- v1.3: Basic security protocols and functions added. Pulled back hashing for the password.

- v1.4 - v1.4.1: getlogbydate, timestamps in general implemented. LogEntry tweaked. More exceptions handled (export, UNIQUE name field for products). Restructured the keystores in the vault file.

- v1.4.2: Forgot to update the log. GUI implemented. Keystores have not yet been seperated as that in 1.4.1 since this was forked from 1.3 initially and worked on offline by Ha. Exception handling has been done on this version as well.
- v1.5: Directory  cleaned. All functions are available. Ready for shipment.

## Set Up Instructions

1. Set up a database on your system or IDE. The default database name is ```shopdb``` and credential is ```root``` and ```root``` for username and password. The default port for the database is ```1527```. You can set the name of the database and credentials differently to what's provided here, but be sure to modify the URL in the ```Database```class and the credentials in the ```Server``` class accordingly.
	- [Eclipse](https://db.apache.org/derby/integrate/plugin_help/start_toc.html)
	- [NetBeans (Native IDE for the project)](https://netbeans.org/kb/docs/ide/java-db.html)
	- [Standalone](http://db.apache.org/derby/papers/DerbyTut/ij_intro.html)
2. Go to the ```db``` directory in the project folder and run *loadShopDB.sql* on your database. This will initialize the database with the required tables. 2 shops will be created by default with their shop IDs being ```01``` and ```02```. The default username and password for the system is ```manager``` and ```123```. You can only change this by querying directly to the database. **This is because the server doesn't control your session and have no user hierarchy at the moment. It would make little sense if any user can delete another's account or add an admin privileged account.** The database admin in this case should be the only one truly with admin privileges. 

3. Build the project and run class "MyLittleShopServer.java" in package ```mylittleshopserver```
4. A console based client is available in package ```mylittleshopclient```. But you might want to use the GUI for full functionality. For this you will need to install [OpenCV](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#introduction-to-opencv-for-java). The link includes a handy guide on how to add OpenCV as a library to your Eclipse for Windows, Mac OS and Linux.
5. You might have noticed that you have to add the jar file to the project **AND**  add the native library location. In case you are using NetBeans on Windows x64:
	- Load up the project in your IDE.
	- Right click the project and choose "Properties"
	- Choose "Run" from the "Categories" list.
	- In the "VM options" field add: ```-Djava.library.path="<Absolute path in your computer to the 'lib' file in the project>"```
	OR
    - Go to ```/nbproject/configs``` in the project folder and open *classpath.properties* in a text editor
    - Add ```run.jvmargs=-Djava.library.path="<Absolute path in your computer to the 'lib' file in the project>"```. Remember to escape the '\\' (backslash) if you use them.
    - In case you are running on a x86 architecture system, you will want to replace *opencv_java2413.dll* in ```lib``` with the one in ```java/x86```. 
    - In case you are on Linux or Mac OS you might need the softlink with the .dylib extension. A guide on getting that is provided at the bottom of the guide provided above, and [here](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#introduction-to-opencv-for-java) once more.

## Keystores

Password to both MLSTrustedKS.ks and MLSServerKS.ks, as well as the associated keypairs is ```2Y9AMGsU4NVjpaxb```if you want to change the SSL encryption algorithm for some reasons (RSA 2048 by default). Changing this means also changing the password properties in ```MyLittleShopClient``` and ```MyLittleShopServer```.