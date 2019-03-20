package grpc.hibernate.manager;

import java.util.stream.Stream;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grpc.balance.BalanceForCurrency;
import grpc.balance.BalanceResponse;
import grpc.balance.BalanceResponse.Builder;
import grpc.hibernate.model.Wallet;
import grpc.hibernate.util.HibernateUtil;
import grpc.hibernate.util.WithdrawException;

public class WalletManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(WalletManager.class);
	private static WalletManager walletManager;

	private WalletManager() {

	}

	public static WalletManager getInstance() {
		if (walletManager == null) {
			walletManager = new WalletManager();
		}
		return walletManager;
	}

	public void withdraw(Wallet wallet) throws WithdrawException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Wallet dbWallet = session.get(Wallet.class, wallet.getKey());

		if (dbWallet == null) {
			session.close();
			throwInsufficientFundsException();
		} else {
			double newAmount = dbWallet.getAmount() - wallet.getAmount();
			if (newAmount >= 0) {
				dbWallet.setAmount(newAmount);
				LOGGER.debug("Withdraw wallet for user id:" + wallet.getKey().getUserId() + ", currency: "
						+ wallet.getKey().getCurrency() + ", new amount: " + dbWallet.getAmount());
				session.update(dbWallet);
			} else {
				session.close();
				throwInsufficientFundsException();
			}
		}

		session.getTransaction().commit();
		session.close();
	}

	private void throwInsufficientFundsException() throws WithdrawException {
		WithdrawException exception = new WithdrawException();
		exception.setMessage("insufficient_funds");
		throw exception;
	}

	public void deposit(Wallet wallet) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Wallet dbWallet = session.get(Wallet.class, wallet.getKey());

		if (dbWallet != null) {
			dbWallet.setAmount(wallet.getAmount() + dbWallet.getAmount());
		} else {
			dbWallet = wallet;
		}

		LOGGER.debug("Saving/updating wallet for user id:" + dbWallet.getKey().getUserId() + ", currency: "
				+ dbWallet.getKey().getCurrency() + ", new amount: " + dbWallet.getAmount());
		session.saveOrUpdate(dbWallet);

		session.getTransaction().commit();
		session.close();
	}

	/**
	 * @param userId
	 * @return all wallets for userId
	 */
	public BalanceResponse read(long userId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Stream<Wallet> wallets = session.createQuery("FROM Wallet where USER_ID = :id", Wallet.class)
				.setParameter("id", userId).stream();

		Builder builder = BalanceResponse.newBuilder();

		wallets.map(wallet -> {
			return BalanceForCurrency.newBuilder().setAmount(wallet.getAmount())
					.setCurrency(wallet.getKey().getCurrency()).build();
		}).forEach(ballanceForCurrency -> builder.addBalanceForCurrency(ballanceForCurrency));

		LOGGER.debug("Found " + builder.getBalanceForCurrencyCount() + " balances for user id: " + userId);

		session.getTransaction().commit();
		session.close();

		return builder.build();
	}
}
