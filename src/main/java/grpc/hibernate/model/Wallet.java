package grpc.hibernate.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import grpc.currency.Currency;

@Entity
public class Wallet {
	@EmbeddedId
	private WalletKey key;

	@Column(nullable = false)
	private double amount;

	public Wallet() {
	}

	public Wallet(WalletKey key, double amount) {
		this.key = key;
		this.amount = amount;
	}

	public Wallet(long userId, Currency currency, double amount) {
		this.key = new WalletKey(userId, currency);
		this.amount = amount;
	}

	public WalletKey getKey() {
		return key;
	}

	public void setKey(WalletKey key) {
		this.key = key;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}