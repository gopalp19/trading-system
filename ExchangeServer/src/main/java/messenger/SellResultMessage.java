package messenger;

import resourcesupport.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Message representing a SELL result message.
 * Created by Alan on 5/7/2017.
 */
public final class SellResultMessage extends ExchangeMessage {
	public Exchange sellerExchange = null;
	public String sellerUserName = null;
	public Stock stock = null;
	public Integer quantitySold = null;
	public Float totalPrice = null;
	public String orderID = null;
    public LocalDateTime timeStamp = null;

    /**
     * Construct a SellResultMessage instance with all fields set to null.
     */
    public SellResultMessage() {
    }

    /**
     * Construct a SellResultMessage instance by specifying all fields.
     * @param sellerExchange home exchange of seller
     * @param sellerUserName identifying username of seller
     * @param stock the Stock that was sold
     * @param quantitySold integer units of stocks sold
     * @param totalPrice total price from selling stocks
     * @param orderID String identifier of order
     */
    public SellResultMessage(Exchange sellerExchange, String sellerUserName, Stock stock, Integer quantitySold, Float totalPrice, String orderID) {
        this.sellerExchange = sellerExchange;
        this.sellerUserName = sellerUserName;
        this.stock = stock;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
        this.orderID = orderID;
    }

    /**
     * Construct a SellResultMessage from a list of Strings
     * @param stringList list of Strings to be parsed into a SellResultMessage
     */
    SellResultMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("SELL_RESULT")) continue;
            int divider = string.indexOf(":");
            String header = string.substring(0, divider).trim();
            String value = string.substring(divider+1).trim();
            switch (header) {
                case "sellerExchange":
                    this.sellerExchange = Exchange.valueOf(value);
                    break;
                case "sellerUserName":
                    this.sellerUserName = value;
                    break;
                case "stock":
                    this.stock = Stock.valueOf(value);
                    break;
                case "quantitySold":
                    this.quantitySold = Integer.parseInt(value);
                    break;
                case "totalPrice":
                    this.totalPrice = Float.parseFloat(value);
                    break;
                case "orderID":
                    this.orderID = value;
                    break;
                case "timeStamp":
                	this.timeStamp = LocalDateTime.parse(value);
                	break;
                default:
                    throw new InputMismatchException("SELL_RESULT header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this SellResultMessage into a list of Strings (without new-line terminators).
     * @return an ArrayList of Strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("SELL_RESULT");
        if (sellerExchange != null) {
            stringList.add("sellerExchange: " + sellerExchange);
        }
        if (sellerUserName != null) {
            stringList.add("sellerUserName: " + sellerUserName);
        }
        if (stock != null) {
            stringList.add("stock: " + stock);
        }
        if (quantitySold != null) {
            stringList.add("quantitySold: " + quantitySold);
        }
        if (totalPrice != null) {
            stringList.add("totalPrice: " + totalPrice);
        }
        if (orderID != null) {
            stringList.add("orderID: " + orderID);
        }
        if (timeStamp != null) {
        	stringList.add("timeStamp: " + timeStamp);
        }
        return stringList;
    }

    /**
     * Get the destination of the message.
     * @return The seller's home exchange
     */
    @Override
    public Exchange getDestination() {
        return sellerExchange;
    }
}
