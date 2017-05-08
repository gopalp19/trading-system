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
    public String textString;

    Continent(String textString) {
        this.textString = textString;
    }

    /**
     * Get the assigned port of this continent
     * @return int holding assigned port number of this continent
     */
    int portNum() {
        return STARTING_PORT + this.ordinal();
    }

}
