package Messenger;

import java.util.InputMismatchException;
import java.util.List;

/**
 * Class that parses strings into Messages, and Messages to strings.
 * Created by Alan on 5/7/2017.
 */
public class MessageBroker {
    /**
     * Construct a message from a list of Strings to be parsed. Do not include the trailing empty string.
     * @param stringList a list of Strings to be parsed
     * @return a Message subclass instance representing parsed String(s)
     * @throws MalformedMessageException if any exception occurs during parsing
     */
    Message parse(List<String> stringList) throws MalformedMessageException {
        try {
            String method = stringList.get(0).trim();
            switch (method) {
                case "BUY":
                    return new BuyMessage(stringList);
                case "SELL":
                    return new SellMessage(stringList);
                case "BUY_RESULT":
                    return new BuyResultMessage(stringList);
                case "SELL_RESULT":
                    return new SellResultMessage(stringList);
                default:
                    throw new InputMismatchException("No valid message has type " + method);
            }
        } catch (Exception cause) {
            MalformedMessageException m = new MalformedMessageException(cause);
            m. printStackTrace();
            throw m;
        }
    }
}
