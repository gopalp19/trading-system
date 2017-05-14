package resourcesupport;

import java.util.ArrayList;
import static resourcesupport.Country.*;

/**
 * Enumeration of exchanges.
 */
public enum Exchange {
    EURONEXT_PARIS("Euronext Paris", FRANCE),
    NEW_YORK_STOCK_EXCHANGE("New York Stock Exchange", USA),
    FRANKFURT("Frankfurt", GERMANY),
    LONDON("London", UK),
    TOKYO("Tokyo", JAPAN),
    HONG_KONG("Hong Kong", CHINA),
    SHANGHAI("Shanghai", CHINA),
    BRUSSELS("Brussels", BELGIUM),
    LISBON("Lisbon", PORTUGAL),
    SHENZHEN("Shenzhen", CHINA),
    TORONTO("Toronto", CANADA),
    BOMBAY("Bombay", INDIA),
    ZURICH("Zurich", SWITZERLAND),
    SYDNEY("Sydney", AUSTRALIA),
    SEOUL("Seoul", KOREA),
    JOHANNESBURG("Johannesburg", SOUTH_AFRICA),
    SAO_PAULO("Sao Paulo", BRAZIL);

    private static final int START_PORT = 52044;

    public String textString;
    private Country country;

    /**
     * private constructor
     */
    Exchange(String textString, Country country) {
        this.textString = textString;
        this.country = country;
    }

    /**
     * Get this exchange's country.
     * @return this exchange's country.
     */
    public Country country() {
        return this.country;
    }

    /**
     * Get this exchange's continent.
     * @return this  exchange's continent.
     */
    public Continent continent() {
        return this.country.continent();
    }

    /**
     * Return the assigned port number of this exchange
     * @return int holding the assigned port number of this exchange
     */
    public int portNum() {
        return START_PORT + this.ordinal();
    }

    /**
     * Return a list of the indices (enum ordinals) for stocks traded on this exchange.
     * @return an ArrayList of indices to stocks traded on this exchange
     */
    public ArrayList<Integer> getStockIndices() {
        ArrayList<Integer> list = new ArrayList<>();
        for (Stock stock : Stock.values()) {
            if (stock.exchange() == this) {
                list.add(stock.ordinal());
            }
        }
        return list;
    }

    /**
     * Return a list of stocks traded on this exchange
     * @return an ArrayList of stocks traded on this exchange
     */
    public ArrayList<Stock> getStocks() {
        ArrayList<Stock> list = new ArrayList<>();
        for (Stock stock : Stock.values()) {
            if (stock.exchange() == this) {
                list.add(stock);
            }
        }
        return list;
    }
}
