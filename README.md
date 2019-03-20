# Project WalletServergRPC

Thhe are two parts - a wallet server and a wallet client. The wallet server will keep track of a users monetary balance in the system. The client will emulate users depositing and withdrawing funds.

Technologies used in this project:
* Java
* gRPC
* Hibernate
* PostgreSQL
* Gradle
* JUnit
* SLF4J

Before installing, make sure you have a postgresql running in localhost:5432. Please, set your user/password and database in hibernate.cfg.xml.



To build the examples, run in this directory:

	./gradlew installDist

First run the server:

	./build/install/WalletServergRPC/bin/grpc-server

And in a different terminal window run the client:

	./build/install/WalletServergRPC/bin/grpc-client users=1 concurrent_threads_per_user=1 rounds_per_thread=1
	

To run the integration test:

	./gradlew test