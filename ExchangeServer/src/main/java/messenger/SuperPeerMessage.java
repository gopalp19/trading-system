package messenger;

public abstract class SuperPeerMessage extends Message {
	// Where is the message going?
	public Continent getDestination();
}
