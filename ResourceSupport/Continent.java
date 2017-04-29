package ResourceSupport;

/**
 * Enumeration of continents
 */
public enum Continent {
    EUROPE("Europe"),
    AMERICA( "America"),
    ASIA("Asia"),
    AFRICA("Africa");

    private static final int STARTING_PORT = 52040;
    String string;

    Continent(String string) {
        this.string = string;
    }

    /**
     * Get the assigned port of this continent
     * @return int holding assigned port number of this continent
     */
    int portNum() {
        return STARTING_PORT + this.ordinal();
    }

    @Override
    public String toString() {
        return this.string;
    }
}
