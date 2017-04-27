package ResourceSupport;

public enum Continent {
    EUROPE("Europe"),
    AMERICA("America"),
    ASIA("Asia"),
    AFRICA("Africa");

    String string;

    Continent(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
