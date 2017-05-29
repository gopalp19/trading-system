package messenger.mutualfundmessage;

import resourcesupport.*;
import java.time.LocalDateTime;
import java.util.*;
import messenger.ExchangeMessage;
import messenger.mutualfundmessage.MutualFundBuyMessage;

/**
 * Message representing a Mutual Fund order result.
 * Only sent from SuperPeer to the local Exchange that placed the order
 * Created by Sam on 5/23/2017.
 */
public final class MutualFundResultMessage extends ExchangeMessage {
    public Exchange buyerExchange = null;
    public String buyerUserName = null;
    public MutualFund fund = null;
    public Integer quantityBought = null;
    public LocalDateTime timeStamp = null;
    public String orderID = null;

    /**
     * Construct a MutualFundResultMessage instance with all fields set to null.
     */
    public MutualFundResultMessage() {
    }

    /**
     * Construct a MutualFundResultMessage instance by specifying its parameters (possibly null).
     * @param buyerExchange home Exchange of buyer
     * @param buyerUserName identifying userName of buyer
     * @param fund Fund to be purchased
     * @param quantity number of units purchased; 0 means failure
     * @param timeStamp LocalDateTime stamp of order
     * @param orderID String identifier of this order
     */
    public MutualFundResultMessage(Exchange buyerExchange, String buyerUserName, MutualFund fund, Integer quantity, LocalDateTime timeStamp, String orderID) {
        this.buyerExchange = buyerExchange;
        this.buyerUserName = buyerUserName;
        this.fund = fund;
        this.quantityBought = quantity;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
    }

    /**
     * Construct a ResultMessage instance from a list of Strings.
     * @param stringList list of Strings to parse into a ResultMessage.
     */
    public MutualFundResultMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("MUTUAL_RESULT")) continue;
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
                case "fund":
                    this.fund = MutualFund.valueOf(value);
                    break;
                case "quantityBought":
                    this.quantityBought = Integer.parseInt(value);
                    break;
                case "timeStamp":
                    this.timeStamp = LocalDateTime.parse(value);
                    break;
                case "orderID":
                    this.orderID = value;
                    break;
                default:
                    throw new InputMismatchException("MUTUAL_RESULT header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Construct a failure MutualFundResultMessage from the original buy message
     * @param buyMessage original buy message
     */
    public MutualFundResultMessage(MutualFundBuyMessage buyMessage) {
        this.buyerExchange = buyMessage.buyerExchange;
        this.buyerUserName = buyMessage.buyerUserName;
        this.fund = buyMessage.fund;
        this.quantityBought = 0;
        this.timeStamp = LocalDateTime.now();
        this.orderID = buyMessage.orderID;
    }


    /**
     * Convert this ResultMessage instance to a list of Strings (without new-line terminators).
     * @return an ArrayList of strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("MUTUAL_RESULT");
        if (buyerExchange != null) {
            stringList.add("buyerExchange: " + buyerExchange);
        }
        if (buyerUserName != null) {
            stringList.add("buyerUserName: " + buyerUserName);
        }
        if (fund != null) {
            stringList.add("fund: " + fund);
        }
        if (quantityBought != null) {
            stringList.add("quantityBought: " + quantityBought);
        }
        if (timeStamp != null) {
            stringList.add("timeStamp: " + timeStamp);
        }
        if (orderID != null) {
            stringList.add("orderID: " + orderID);
        }
        return stringList;
    }

    /**
     * Get the destination of the message.
     * @return The Exchange where the stock is sold
     */
    @Override
    public Exchange getDestination() {
        return buyerExchange;
    }

}
