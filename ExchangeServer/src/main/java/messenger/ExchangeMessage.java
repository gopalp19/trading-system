package messenger;
import resourcesupport.Exchange;

public abstract class ExchangeMessage extends Message {
	// Where is the message going?
	public abstract Exchange getDestination();
}
