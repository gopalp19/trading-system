package Messenger;

import ResourceSupport.*;
import java.util.*;

/**
 * Message representing a SELL result message.
 * Created by Alan on 5/7/2017.
 */
public final class SellResultMessage extends Message {
    Exchange sellerExchange = null;
    Integer sellerID = null;
    Stock stock = null;
    Integer quantitySold = null;
    Float totalPrice = null;
    String orderID = null;

    /**
     * Construct a SellResultMessage instance with all fields set to null.
     */
    public SellResultMessage() {
    }

    /**
     * Construct a SellResultMessage instance by specifying all fields.
     * @param sellerExchange home exchange of seller
     * @param sellerID identifying integer of seller
     * @param stock the Stock that was sold
     * @param quantitySold integer units of stocks sold
     * @param totalPrice total price from selling stocks
     * @param orderID String identifier of order
     */
    public SellResultMessage(Exchange sellerExchange, Integer sellerID, Stock stock, Integer quantitySold, Float totalPrice, String orderID) {
        this.sellerExchange = sellerExchange;
        this.sellerID = sellerID;
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
                case "sellerID":
                    this.sellerID = Integer.parseInt(value);
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
        if (sellerID != null) {
            stringList.add("sellerID: " + sellerID);
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
        return stringList;
    }
}
