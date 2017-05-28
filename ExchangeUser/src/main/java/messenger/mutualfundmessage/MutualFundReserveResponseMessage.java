package messenger.mutualfundmessage;

import resourcesupport.*;
import java.time.LocalDateTime;
import java.util.*;
import messenger.SuperPeerMessage;

/**
 * Message representing a MUTAL_RESERVE_RESPONSE order.
 * Created by Sam on 5/25/2017.
 */
public final class MutualFundReserveResponseMessage extends SuperPeerMessage {
    public Continent superpeer = null;
    public Stock stock = null;
    public Integer quantity = null;
    public LocalDateTime timeStamp = null;
    public String orderID = null;
    public Boolean reservationConfirmed = null;

    /**
     * Construct a MutualFundReserveResponseMessage instance with all fields set to null.
     */
    public MutualFundReserveResponseMessage() {
    }

    /**
     * Construct a MutualFundReserveResponseMessage instance by specifying its parameters (possibly null).
     * @param superpeer SuperPeer trying to reserve the stocks
     * @param stock Stock to be reserved
     * @param quantity number of stocks to be reserved
     * @param timeStamp LocalDateTime stamp of order
     * @param orderID String identifier of this order
     * @param reservationConfirmed whether the reservation has been made
     */
    public MutualFundReserveResponseMessage(Continent superpeer, Stock stock, Integer quantity, 
            LocalDateTime timeStamp, String orderID, Boolean reservationConfirmed) 
    {
        this.superpeer = superpeer;
        this.stock = stock;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
        this.reservationConfirmed = reservationConfirmed;
    }

    /**
     * Construct a MutualFundReserveResponseMessage instance from a list of Strings.
     * @param stringList list of Strings to parse into a MutualFundReserveResponseMessage.
     */
    public MutualFundReserveResponseMessage(List<String> stringList) {
        for (String string : stringList) {
            if (string.equals("MUTUAL_RESERVE_RESPONSE")) continue;
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
                case "reservationConfirmed":
                    this.reservationConfirmed = Boolean.valueOf(value);
                    break;
                default:
                    throw new InputMismatchException("MUTUAL_RESERVE_RESPONSE header \"" + header + "\" not recognized");
            }
        }
    }

    /**
     * Convert this MutualFundReserveResponseMessage instance to a list of Strings (without new-line terminators).
     * @return an ArrayList of strings representing the information in this message instance.
     */
    @Override
    public ArrayList<String> toStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("MUTUAL_RESERVE_RESPONSE");
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
        if (reservationConfirmed != null) {
            stringList.add("reservationConfirmed: " + reservationConfirmed);
        }
        return stringList;
    }

    /**
     * Get the destination of the message.
     * @return The Exchange where the stock is sold
     */
    @Override
    public Continent getDestination() {
        return superpeer;
    }

}
