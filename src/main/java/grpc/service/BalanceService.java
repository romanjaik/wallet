package grpc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grpc.balance.BalanceGrpc;
import grpc.balance.BalanceRequest;
import grpc.balance.BalanceResponse;
import grpc.hibernate.manager.WalletManager;
import io.grpc.stub.StreamObserver;

public class BalanceService extends BalanceGrpc.BalanceImplBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(BalanceService.class);

	@Override
	public void getBalance(BalanceRequest req, StreamObserver<BalanceResponse> responseObserver) {
		responseObserver.onNext(getBalance(req));
		responseObserver.onCompleted();
	}

	private BalanceResponse getBalance(BalanceRequest balanceRequest) {
		LOGGER.debug("Balance Request for user id: " + balanceRequest.getUserId());
		return WalletManager.getInstance().read(balanceRequest.getUserId());
	}
}
