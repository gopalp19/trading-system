package ResourceSupport;

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
}
