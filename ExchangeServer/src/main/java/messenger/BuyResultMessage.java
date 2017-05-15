package messenger;

import resourcesupport.*;
import java.util.*;

/**
 * Message representing a BUY result message.
 * Created by Alan on 5/7/2017.
 */
public final class BuyResultMessage extends Message {
    Exchange buyerExchange = null;
    String buyerUserName = null;
    Stock stock = null;
    Integer quantityBought = null;
    Float totalPrice = null;
    String orderID = null;

    /**
     * Construct a BuyResultMessage instance with all fields set to null.
     */
    public BuyResultMessage() {
    }

    /**
     * Construct a BuyResultMessage instance by specifying all fields.
     * @param buyerExchange home exchange of buyer
     * @param buyerUserName identifying userName of buyer
     * @param stock the Stock that was bought
     * @param quantityBought integer units of stocks purchased
     * @param totalPrice total price of purchasing stocks
     * @param orderID String identifier of order
     */
    public BuyResultMessage(Exchange buyerExchange, String buyerUserName, Stock stock, Integer quantityBought, Float totalPrice, String orderID) {
        this.buyerExchange = buyerExchange;
        this.buyerUserName = buyerUserName;
        this.stock = stock;
        this.quantityBought = quantityBought;
        this.totalPrice = totalPrice;
        this.orderID = orderID;
    }

    /**
     * Construct a BuyResultMessage from a list of Strings
     * @param stringList list of Strings to be parsed into a BuyResultMessage
     */
    BuyResultMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("BUY_RESULT")) continue;
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
                case "quantityBought":
                    this.quantityBought = Integer.parseInt(value);
                    break;
                case "totalPrice":
                    this.totalPrice = Float.parseFloat(value);
                    break;
                case "orderID":
                    this.orderID = value;
                    break;
                default:
                    throw new InputMismatchException("BUY_RESULT header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this BuyResultMessage into a list of Strings (without new-line terminators).
     * @return an ArrayList of Strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("BUY_RESULT");
        if (buyerExchange != null) {
            stringList.add("buyerExchange: " + buyerExchange);
        }
        if (buyerUserName != null) {
            stringList.add("buyerUserName: " + buyerUserName);
        }
        if (stock != null) {
            stringList.add("stock: " + stock);
        }
        if (quantityBought != null) {
            stringList.add("quantityBought: " + quantityBought);
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
