package ResourceSupport;

import static ResourceSupport.Continent.*;

/**
 * Enumeration of countries.
 */
public enum Country {
    FRANCE("France", EUROPE),
    USA("USA", AMERICA),
    GERMANY("Germany", EUROPE),
    UK("UK", EUROPE),
    JAPAN("Japan", ASIA),
    CHINA("China", ASIA),
    BELGIUM("Belgium", EUROPE),
    PORTUGAL("Portugal", EUROPE),
    CANADA("Canada", AMERICA),
    INDIA("India", ASIA),
    SWITZERLAND("Switzerland", EUROPE),
    AUSTRALIA("Australia", ASIA),
    KOREA("Korea", ASIA),
    SOUTH_AFRICA("South Africa", AFRICA),
    BRAZIL("Brazil", AMERICA);

    private Continent continent;
    public String textString;

    Country(String textString, Continent continent) {
        this.continent = continent;
        this.textString = textString;
    }

    /**
     * Get this country's continent.
     * @return this country's continent
     */
    public Continent continent() {
        return this.continent;
    }

}

