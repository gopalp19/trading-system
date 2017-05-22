package messenger;
import resourcesupport.Continent;

public abstract class SuperPeerMessage extends Message {
	// Where is the message going?
	public abstract Continent getDestination();
}
