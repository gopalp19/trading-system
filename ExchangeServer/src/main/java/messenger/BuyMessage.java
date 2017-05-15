package messenger;

import resourcesupport.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Message representing a BUY order.
 * Created by Alan on 5/7/2017.
 */
public final class BuyMessage extends Message {
    Exchange buyerExchange = null;
    String buyerUserName = null;
    Stock stock = null;
    Integer quantity = null;
    LocalDateTime timeStamp = null;
    String orderID = null;

    /**
     * Construct a BuyMessage instance with all fields set to null.
     */
    public BuyMessage() {
    }

    /**
     * Construct a BuyMessage instance by specifying its parameters (possibly null).
     * @param buyerExchange home Exchange of buyer
     * @param buyerUserName identifying userName of buyer
     * @param stock Stock to be purchased
     * @param quantity number of stocks to be purchased
     * @param timeStamp LocalDateTime stamp of order
     * @param orderID String identifier of this order
     */
    public BuyMessage(Exchange buyerExchange, String buyerUserName, Stock stock, Integer quantity, LocalDateTime timeStamp, String orderID) {
        this.buyerExchange = buyerExchange;
        this.buyerUserName = buyerUserName;
        this.stock = stock;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
    }

    /**
     * Construct a BuyMessage instance from a list of Strings.
     * @param stringList list of Strings to parse into a BuyMessage.
     */
    public BuyMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("BUY")) continue;
            int divider = string.indexOf(":");
            String header = string.substring(0, divider).trim();
            String value = string.substring(divider+1).trim();
            switch (header) {
                case "buyerExchange":
                    this.buyerExchange = Exchange.valueOf(value);
                    break;
                case "buyerUserName":
                    this.buyerUserName = value;
                    break;
                case "stock":
                    this.stock = Stock.valueOf(value);
                    break;
                case "quantity":
                    this.quantity = Integer.parseInt(value);
                    break;
                case "timeStamp":
                    this.timeStamp = LocalDateTime.parse(value);
                    break;
                case "orderID":
                    this.orderID = value;
                    break;
                default:
                    throw new InputMismatchException("BUY header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this BuyMessage instance to a list of Strings (without new-line terminators).
     * @return an ArrayList of strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("BUY");
        if (buyerExchange != null) {
            stringList.add("buyerExchange: " + buyerExchange);
        }
        if (buyerUserName != null) {
            stringList.add("buyerUserName: " + buyerUserName);
        }
        if (stock != null) {
            stringList.add("stock: " + stock);
        }
        if (quantity != null) {
            stringList.add("quantity: " + quantity);
        }
        if (timeStamp != null) {
            stringList.add("timestamp: " + timeStamp);
        }
        if (orderID != null) {
            stringList.add("orderID: " + orderID);
        }
        return stringList;
    }
}
