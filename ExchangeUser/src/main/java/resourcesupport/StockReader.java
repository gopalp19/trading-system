package resourcesupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Class with functions to support reading the project's csv files.
 * Created by Alan on 4/27/2017.
 */
public class StockReader {
    private static final String PRICE_PATH = "price_stocks.csv";
    private static final String QUANTITY_PATH = "qty_stocks.csv";
    private static final int OFFSET_ROWS = 4; // first four rows of .csv are heading
    private static final int OFFSET_COLUMNS = 3; // first three columns of .csv are heading

    /**
     * For an exchange, get a Hashtable mapping each of its stocks to a list of its prices at various times.
     * @param exchange - the exchange whose stocks will be examined
     * @return a Hashtable mapping from Stock to list of pricing info
     */
    public static Hashtable<Stock, ArrayList<PriceInstance>> getPrices(Exchange exchange) {
        File file = new File(PRICE_PATH);
        Hashtable<Stock, ArrayList<PriceInstance>> prices = new Hashtable<>();
        ArrayList<Integer> indices = exchange.getStockIndices();
        Stock[] stockArray = Stock.values();

        for (Integer i : indices) {
            prices.put(stockArray[i], new ArrayList<>());
        }
        try (Scanner fileScan = new Scanner(file)) {
            for (int i = 0; i < OFFSET_ROWS; i++) {
                fileScan.nextLine();
            }
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] terms = line.split(",", -1);
                LocalDateTime time = getTime(terms[0], terms[1]);
                for (Integer i : indices) {
                    PriceInstance pi = new PriceInstance(stockArray[i], time, Float.parseFloat(terms[i + OFFSET_COLUMNS]));
                    prices.get(stockArray[i]).add(pi);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: FileNotFoundException for file " + PRICE_PATH);
            System.exit(1);
        }
        return prices;
    }

    /**
     * For an exchange, get a Hashtable mapping each of its stocks to the initial quantity info.
     * @param exchange - the exchange whose stocks will be examined
     * @return a Hashtable mapping from Stock to IPO info
     */
    public static Hashtable<Stock, IPO> getQuantities(Exchange exchange) {
        File file = new File(QUANTITY_PATH);
        Hashtable<Stock, IPO> quantities = new Hashtable<>();
        ArrayList<Integer> indices = exchange.getStockIndices();
        Stock[] stockArray = Stock.values();

        try (Scanner fileScan = new Scanner(file)) {
            for (int i = 0; i < OFFSET_ROWS; i++) {
                fileScan.nextLine();
            }
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] terms = line.split(",", -1);
                LocalDateTime time = getTime(terms[0], terms[1]);
                for (Integer i : indices) {
                    if (!terms[i + OFFSET_COLUMNS].isEmpty()) {
                        IPO ipo = new IPO(stockArray[i], time, Integer.parseInt(terms[i + OFFSET_COLUMNS]));
                        quantities.put(stockArray[i], ipo);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: FileNotFoundException for file " + QUANTITY_PATH);
            System.exit(1);
        }
        return quantities;
    }

    private static LocalDateTime getTime(String date, String time) {
        String[] dateTerms = date.split("/");
        int mo = Integer.parseInt(dateTerms[0]);
        int day = Integer.parseInt(dateTerms[1]);
        int yr = Integer.parseInt(dateTerms[2]);
        String[] timeTerms = time.split(":");
        int hr = Integer.parseInt(timeTerms[0]);
        int min = Integer.parseInt(timeTerms[1]);
        String formattedDate = (new Formatter()).format("%02d-%02d-%02d", yr, mo, day).toString();
        String formattedTime = (new Formatter()).format("%02d:%02d", hr, min).toString();
        return LocalDateTime.parse(formattedDate + "T" + formattedTime);
    }
}
