package resourcesupport;

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
    public int portNum() {
        return STARTING_PORT + this.ordinal();
    }
    
    /**
     * Get an array of continents this continent is connected to.
     * @return an array of continents this continent is connected to.
     */
    public Continent[] neighbors() {
    	switch (this) {
            case EUROPE:
                return new Continent[] {AMERICA, ASIA};
    	    case AMERICA:
    	    	return new Continent[] {EUROPE};
    	    case ASIA:
    	    	return new Continent[] {EUROPE, AFRICA};
    	    case AFRICA:
    	    	return new Continent[] {ASIA};
    		default:
    			return null;
    	}
    }

}
