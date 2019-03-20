package grpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcMainClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrpcMainClient.class);

	/**
	 * localhost:50051
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			LOGGER.error(
					"Total of arguments is incorrect. Client requires 3 arguments: users, concurrent_threads_per_user, and rounds_per_thread");
			System.exit(1);
		}

		int users = 0;
		int concurrentThreadsPerUser = 0;
		int roundsPerThread = 0;

		for (String arg : args) {
			String[] parameterValue = arg.split("=");

			if (parameterValue.length != 2) {
				LOGGER.error(
						"Invalid parameter to main. Should use: users=value concurrent_threads_per_user=value rounds_per_thread=value");
				System.exit(1);
			}

			String parameter = parameterValue[0];
			int value = 0;
			try {
				value = Integer.parseInt(parameterValue[1]);
			} catch (NumberFormatException exception) {
				LOGGER.error("Invalid value: " + value + ". Value should be an integer.");
				System.exit(1);
			}

			if (parameter.equals("users")) {
				users = value;
				LOGGER.info("users: " + users);
			} else if (parameter.equals("concurrent_threads_per_user")) {
				concurrentThreadsPerUser = value;
				LOGGER.info("concurrent_threads_per_user: " + concurrentThreadsPerUser);
			} else if (parameter.equals("rounds_per_thread")) {
				roundsPerThread = value;
				LOGGER.info("rounds_per_thread: " + roundsPerThread);
			} else {
				LOGGER.error("Invalid parameter: " + parameter);
				System.exit(1);
			}
		}

		GrpcClient client = new GrpcClient("localhost", 50051);

		for (int user = 1; user <= users; user++) {
			for (int thread = 1; thread <= concurrentThreadsPerUser; thread++) {
				RoundThread roundThread = new RoundThread(user, roundsPerThread, client);
				roundThread.start();
			}
		}
	}
}
