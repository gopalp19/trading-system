package messenger;

import java.util.ArrayList;
import resourcesupport.Exchange;

/**
 *
 * Created by Alan on 5/7/2017.
 */
public abstract class Message {
    public abstract ArrayList<String> toStringList();

    // Added for testing purposes
    public void print() {
    	ArrayList<String> lines = toStringList();

    	for (String line : lines) {
    		System.out.println(line);
    	}
    }

}
