package resourcesupport;

/**
 * Enumeration of Mutual Funds.
 * Created by Alan on 4/27/2017.
 */
public enum MutualFund {
    BANKING(new Stock[] {Stock.DEUTSCHE_BANK, Stock.CREDIT_AGRICOLE, Stock.SOCIETE_GENERALE, Stock.AMERICAN_EXPRESS, Stock.GOLDMAN_SACHS, Stock.JPMORGAN_CHASE, Stock._NOMURA_HOLDINGS__INC__},
            new int[] {20, 20, 10, 20, 10, 15, 5}, 20, "BANKING"),
    ENERGY(new Stock[] {Stock.PETROBRAS, Stock.BP_PLC, Stock.TOTAL, Stock.EXXONMOBIL},
            new int[] {15, 15, 40, 30}, 20, "ENERGY"),
    DIVERSIFIED(new Stock[] {Stock.SWIRE_PACIFIC_LIMITED, Stock.SOFTBANK_CORP_, Stock.SKY_PLC, Stock.DEUTSCHE_LUFTHANSA},
            new int[] {15, 35, 40, 10}, 20, "DIVERSIFIED");

    Stock[] stocks;
    int[] weights;
    int minimumBlock; // any number of mutual fund units that are transacted must be divisible by this number
    public String textString;

    MutualFund(Stock[] stocks, int[] weights, int minimumBlock, String textString) {
        this.stocks = stocks;
        this.weights = weights;
        this.minimumBlock = minimumBlock;
        this.textString = textString;
    }
}
