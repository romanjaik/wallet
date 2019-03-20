package grpc.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grpc.hibernate.util.HibernateUtil;
import grpc.service.BalanceService;
import grpc.service.DepositService;
import grpc.service.WithdrawService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
	private Server server;

	public void start() throws IOException {

		int port = 50051;
		LOGGER.info("Starting server in port: " + port);
		server = ServerBuilder.forPort(port).addService(new DepositService()).addService(new BalanceService())
				.addService(new WithdrawService()).build().start();

		LOGGER.info("Server started with services: " + server.getServices());

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				GrpcServer.this.stop();
			}
		});
	}

	public void stop() {
		if (server != null) {
			LOGGER.info("Shutting down server");
			server.shutdown();
		}
		LOGGER.info("Server shut down");
		LOGGER.info("Shutting down Hibernate");
		HibernateUtil.shutdown();
		LOGGER.info("Hibernate shut down");
	}

	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		final GrpcServer server = new GrpcServer();
		server.start();
		server.blockUntilShutdown();
	}
}