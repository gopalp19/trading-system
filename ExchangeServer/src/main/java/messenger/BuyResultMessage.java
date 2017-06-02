package messenger;

import resourcesupport.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Message representing a BUY result message.
 * Created by Alan on 5/7/2017.
 */
public final class BuyResultMessage extends ExchangeMessage {
    public Exchange buyerExchange = null;
    public String buyerUserName = null;
    public Stock stock = null;
    public Integer quantityBought = null;
    public Float totalPrice = null;
    public String orderID = null;
    public LocalDateTime timeStamp = null;
    public String notificationMessage = null;

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
                case "timeStamp":
                	this.timeStamp = LocalDateTime.parse(value);
                	break;
                case "message":
                	this.notificationMessage = value;
                	break;
                default:
                    throw new InputMismatchException("BUY_RESULT header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Construct a failure BuyResultMessage from the corresponding BuyMessage
     * @param request The original BuyMessage
     */
    public BuyResultMessage(BuyMessage request) {
        this.buyerExchange = request.buyerExchange;
        this.buyerUserName = request.buyerUserName;
        this.stock = request.stock;
        this.quantityBought = 0;
        this.totalPrice = (float) 0;
        this.timeStamp = LocalDateTime.now();
        this.orderID = request.orderID;
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
        if (timeStamp != null) {
        	stringList.add("timeStamp: " + timeStamp);
        }
        if (notificationMessage != null) {
        	stringList.add("message: " + notificationMessage);
        }
        return stringList;
    }

    /**
     * Get the destination of the message.
     * @return The buyer's exchange
     */
    @Override
    public Exchange getDestination() {
        return buyerExchange;
    }
}
