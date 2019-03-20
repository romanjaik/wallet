package grpc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import grpc.hibernate.manager.WalletManager;
import grpc.hibernate.model.Wallet;
import grpc.hibernate.util.WithdrawException;
import grpc.withdraw.WithdrawGrpc;
import grpc.withdraw.WithdrawRequest;
import grpc.withdraw.WithdrawResponse;
import io.grpc.stub.StreamObserver;

public class WithdrawService extends WithdrawGrpc.WithdrawImplBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawService.class);

	@Override
	public void withdraw(WithdrawRequest req, StreamObserver<WithdrawResponse> responseObserver) {
		responseObserver.onNext(withdraw(req));
		responseObserver.onCompleted();
	}

	private WithdrawResponse withdraw(WithdrawRequest withdrawRequest) {
		LOGGER.debug("Withdraw Request for user id: " + withdrawRequest.getUserId() + ", amount: "
				+ withdrawRequest.getAmount());

		if (withdrawRequest.getCurrency() == null) {
			LOGGER.debug("Withdraw for user id: " + withdrawRequest.getUserId() + " is missing currency");
			return WithdrawResponse.newBuilder().setErrorMessage("Missing Currency").build();
		}

		Wallet wallet = new Wallet(withdrawRequest.getUserId(), withdrawRequest.getCurrency(),
				withdrawRequest.getAmount());

		try {
			WalletManager.getInstance().withdraw(wallet);
		} catch (WithdrawException e) {
			LOGGER.debug("Withdraw for user id: " + withdrawRequest.getUserId() + " could not be completed: "
					+ e.getMessage());
			return WithdrawResponse.newBuilder().setErrorMessage(e.getMessage()).build();
		}

		return WithdrawResponse.newBuilder().setErrorMessage("ok").build();
	}
}