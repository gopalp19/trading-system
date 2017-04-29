package ResourceSupport;

import java.util.ArrayList;

/**
 * Enumeration of exchanges.
 */
public enum Exchange {
    EURONEXT_PARIS("Euronext Paris", Continent.EUROPE, Country.FRANCE),
    NEW_YORK_STOCK_EXCHANGE("New York Stock Exchange", Continent.AMERICA, Country.USA),
    FRANKFURT("Frankfurt", Continent.EUROPE, Country.GERMANY),
    LONDON("London", Continent.EUROPE, Country.UK),
    TOKYO("Tokyo", Continent.ASIA, Country.JAPAN),
    HONG_KONG("Hong Kong", Continent.ASIA, Country.CHINA),
    SHANGHAI("Shanghai", Continent.ASIA, Country.CHINA),
    BRUSSELS("Brussels", Continent.EUROPE, Country.BELGIUM),
    LISBON("Lisbon", Continent.EUROPE, Country.PORTUGAL),
    SHENZHEN("Shenzhen", Continent.ASIA, Country.CHINA),
    TORONTO("Toronto", Continent.AMERICA, Country.CANADA),
    BOMBAY("Bombay", Continent.ASIA, Country.INDIA),
    ZURICH("Zurich", Continent.EUROPE, Country.SWITZERLAND),
    SYDNEY("Sydney", Continent.ASIA, Country.AUSTRALIA),
    SEOUL("Seoul", Continent.ASIA, Country.KOREA),
    JOHANNESBURG("Johannesburg", Continent.AFRICA, Country.SOUTH_AFRICA),
    SAO_PAULO("Sao Paulo", Continent.AMERICA, Country.BRAZIL);

    private static final int START_PORT = 52044;

    String string;
    Continent continent;
    Country country;

    Exchange(String string, Continent continent, Country country) {
        this.string = string;
        this.continent = continent;
        this.country = country;
    }

    @Override
    public String toString() {
        return this.string;
    }

    /**
     * Return the assigned port number of this exchange
     * @return int holding the assigned port number of this exchange
     */
    int portNum() {
        return START_PORT + this.ordinal();
    }

    /**
     * Return a list of the indices (enum ordinals) for stocks traded on this exchange.
     * @return an ArrayList of indices to stocks traded on this exchange
     */
    ArrayList<Integer> getStockIndices() {
        ArrayList<Integer> list = new ArrayList<>();
        for (Stock stock : Stock.values()) {
            if (stock.exchange == this) {
                list.add(stock.ordinal());
            }
        }
        return list;
    }

    /**
     * Return a list of stocks traded on this exchange
     * @return an ArrayList of stocks traded on this exchange
     */
    ArrayList<Stock> getStocks() {
        ArrayList<Stock> list = new ArrayList<>();
        for (Stock stock : Stock.values()) {
            if (stock.exchange == this) {
                list.add(stock);
            }
        }
        return list;
    }
}
