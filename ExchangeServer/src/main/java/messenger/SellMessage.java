package messenger;

import resourcesupport.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Message representing a SELL order.
 * Created by Alan on 5/7/2017.
 */
public final class SellMessage extends Message {
    public Exchange sellerExchange = null;
    public String sellerUserName = null;
    public Stock stock = null;
    public Integer quantity = null;
    public LocalDateTime timeStamp = null;
    public String orderID = null;

    /**
     * Construct a SellMessage instance with all fields set to null.
     */
    public SellMessage() {
    }

    /**
     * Construct a SellMessage instance by specifying its parameters (possibly null).
     * @param sellerExchange home Exchange of seller
     * @param sellerUserName identifying username of seller
     * @param stock Stock to be sold
     * @param quantity number of stocks to be sold
     * @param timeStamp LocalDateTime stamp of order
     * @param orderID String identifier of this order
     */
    public SellMessage(Exchange sellerExchange, String sellerUserName, Stock stock, Integer quantity, LocalDateTime timeStamp, String orderID) {
        this.sellerExchange = sellerExchange;
        this.sellerUserName = sellerUserName;
        this.stock = stock;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
        this.destination = stock.exchange();
    }

    /**
     * Construct a SellMessage instance from a list of Strings.
     * @param stringList list of Strings to parse into a SellMessage.
     */
    public SellMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("SELL")) continue;
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
                    throw new InputMismatchException("SELL header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this SellMessage instance to a list of Strings (without new-line terminators).
     * @return an ArrayList of strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("SELL");
        if (sellerExchange != null) {
            stringList.add("sellerExchange: " + sellerExchange);
        }
        if (sellerUserName != null) {
            stringList.add("sellerUserName: " + sellerUserName);
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
