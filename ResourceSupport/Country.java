package ResourceSupport;

public enum Country {
    FRANCE("France", Continent.EUROPE),
    USA("USA", Continent.AMERICA),
    GERMANY("Germany", Continent.EUROPE),
    UK("UK", Continent.EUROPE),
    JAPAN("Japan", Continent.ASIA),
    CHINA("China", Continent.ASIA),
    BELGIUM("Belgium", Continent.EUROPE),
    PORTUGAL("Portugal", Continent.EUROPE),
    CANADA("Canada", Continent.AMERICA),
    INDIA("India", Continent.ASIA),
    SWITZERLAND("Switzerland", Continent.EUROPE),
    AUSTRALIA("Australia", Continent.ASIA),
    KOREA("Korea", Continent.ASIA),
    SOUTH_AFRICA("South Africa", Continent.AFRICA),
    BRAZIL("Brazil", Continent.AMERICA);

    Continent continent;
    String string;

    Country(String string, Continent continent) {
        this.continent = continent;
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}

