package messenger;

public abstract class ExchangeMessage extends Message {
	// Where is the message going?
	public Exchange getDestination();
}
