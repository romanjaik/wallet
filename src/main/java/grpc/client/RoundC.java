package grpc.client;

import grpc.balance.BalanceRequest;
import grpc.currency.Currency;
import grpc.deposit.DepositRequest;
import grpc.withdraw.WithdrawRequest;

public class RoundC extends AbstractRound {

	public RoundC(GrpcClient client) {
		super(client);
	}

	@Override
	void runRound(long userId) {
		// Get Balance
		client.getBalanceBlockingStub().getBalance(BalanceRequest.newBuilder().setUserId(userId).build());
		// Deposit 100 USD
		client.getDepoistBlockingStub().doDeposit(
				DepositRequest.newBuilder().setAmount(100).setCurrency(Currency.USD).setUserId(userId).build());
		// Deposit 100 USD
		client.getDepoistBlockingStub().doDeposit(
				DepositRequest.newBuilder().setAmount(100).setCurrency(Currency.USD).setUserId(userId).build());
		// Withdraw 100 USD
		client.getWithdrawBlockingStub().withdraw(
				WithdrawRequest.newBuilder().setAmount(100).setCurrency(Currency.USD).setUserId(userId).build());
		// Deposit 100 USD
		client.getDepoistBlockingStub().doDeposit(
				DepositRequest.newBuilder().setAmount(100).setCurrency(Currency.USD).setUserId(userId).build());
		// Get Balance
		client.getBalanceBlockingStub().getBalance(BalanceRequest.newBuilder().setUserId(userId).build());
		// Withdraw 100 USD
		client.getWithdrawBlockingStub().withdraw(
				WithdrawRequest.newBuilder().setAmount(200).setCurrency(Currency.USD).setUserId(userId).build());
		// Get Balance
		client.getBalanceBlockingStub().getBalance(BalanceRequest.newBuilder().setUserId(userId).build());
	}
}
