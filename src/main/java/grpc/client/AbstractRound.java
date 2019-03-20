package grpc.client;

public abstract class AbstractRound {
	protected GrpcClient client;

	public AbstractRound(GrpcClient client) {
		this.client = client;
	}

	abstract void runRound(long userId);
}
