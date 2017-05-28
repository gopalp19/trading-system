package messenger.mutualfundmessage;

import resourcesupport.*;
import java.time.LocalDateTime;
import java.util.*;
import messenger.ExchangeMessage;

/**
 * Message representing a MUTUAL_UPDATE order.
 * Superpeer will tell destination exchange to either commit to
 * the reserved stock or to release the reserved stock
 * Created by Sam on 5/25/2017.
 */

public final class MutualFundUpdateMessage extends ExchangeMessage {
    public Continent superpeer = null;
    public Stock stock = null;
    public Integer quantity = null;
    public LocalDateTime timeStamp = null;
    public String orderID = null;
    public Boolean doCommit = null;

    /**
     * Construct a MutualFundUpdateMessage instance with all fields set to null.
     */
    public MutualFundUpdateMessage() {
    }

    /**
     * Construct a MutualFundUpdateMessage instance by specifying its parameters (possibly null).
     * @param superpeer SuperPeer reserving the stocks
     * @param stock Stock to be reserved
     * @param quantity number of stocks to be reserved
     * @param timeStamp LocalDateTime stamp of order
     * @param orderID String identifier of this order
     * @param doCommit Whether the exchange should commit (true) or release (false)
     */
    public MutualFundUpdateMessage(Continent superpeer, Stock stock, Integer quantity, LocalDateTime timeStamp, String orderID, Boolean doCommit) {
        this.superpeer = superpeer;
        this.stock = stock;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
        this.doCommit = doCommit;
    }

    /**
     * Construct a MutualFundUpdateMessage instance from a list of Strings.
     * @param stringList list of Strings to parse into a MutualFundUpdateMessage.
     */
    public MutualFundUpdateMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("MUTUAL_UPDATE")) continue;
            int divider = string.indexOf(":");
            String header = string.substring(0, divider).trim();
            String value = string.substring(divider+1).trim();
            switch (header) {
                case "superpeer":
                    this.superpeer = Continent.valueOf(value);
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
                case "doCommit":
                    this.doCommit = Boolean.valueOf(value);
                    break;
                default:
                    throw new InputMismatchException("MUTUAL_UPDATE header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this MutualFundUpdateMessage instance to a list of Strings (without new-line terminators).
     * @return an ArrayList of strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("MUTUAL_UPDATE");
        if (superpeer != null) {
            stringList.add("superpeer: " + superpeer);
        }
        if (stock != null) {
            stringList.add("stock: " + stock);
        }
        if (quantity != null) {
            stringList.add("quantity: " + quantity);
        }
        if (timeStamp != null) {
            stringList.add("timeStamp: " + timeStamp);
        }
        if (orderID != null) {
            stringList.add("orderID: " + orderID);
        }
        if (doCommit != null) {
            stringList.add("doCommit: " + doCommit);
        }
        return stringList;
    }

    /**
     * Get the destination of the message.
     * @return The Exchange where the stock is sold
     */
    @Override
    public Exchange getDestination() {
        return stock.exchange();
    }

}
