package com.mpcs.distributed.systems;

import resourcesupport.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import superpeer.SuperPeer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.mpcs.distributed.systems.application.AppContext;
import com.mpcs.distributed.systems.model.StockPrice;
import com.mpcs.distributed.systems.model.StockQuantity;
import com.mpcs.distributed.systems.model.User;
import com.mpcs.distributed.systems.repositories.StockPriceRepository;
import com.mpcs.distributed.systems.repositories.StockQuantityRepository;
import com.mpcs.distributed.systems.repositories.UserRepository;
import com.mpcs.distributed.systems.services.ClientConnection;

@SpringBootApplication
public class ExchangeServer extends SpringBootServletInitializer {
	public static ExchangeTimer exchangeTimer = new ExchangeTimer();
	public static SuperPeer superPeer = null;
	public static ClientReplier clientReplier;
	public static SenderToSuper senderToSuper;
	public static Exchange[] neighborPeers = null;
    public static Exchange exchange = null;
    public static Logger logger = null;
    public static SuperChecker superChecker = null;
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ExchangeServer.class);
	}
	
	@Bean
	public CommandLineRunner initBeans(ApplicationContext ctx, UserRepository userRepository, StockPriceRepository stockPriceRepository, StockQuantityRepository stockQuantityRepository) {
		return (args) -> {
            AppContext.setApplicationContext(ctx);
			userRepository.save(new User("gopalp", exchange.toString()));
			userRepository.save(new User("anon", exchange.toString()));
			int count = 1;
			while(count <= 10){
				userRepository.save(new User("user" + count, exchange.name()));
				count++;
			}
	        //Hashtable<Stock, ArrayList<PriceInstance>> pricesTable = StockReader.getPrices(exchange);
	        //Hashtable<Stock, IPO> quantitiesTable = StockReader.getQuantities(exchange);
	        //initData(pricesTable, stockPriceRepository, quantitiesTable, stockQuantityRepository);
		};
	}

	public static void main(String[] args) {
		String exchangeServerName = null;
        try {
        	exchangeServerName = args[0];
        	exchange = Exchange.valueOf(exchangeServerName);
        	neighborPeers = exchange.neighbors();
        	senderToSuper = new SenderToSuper(exchange);
        	clientReplier = new ClientReplier();
        	logger = new Logger();
        	superChecker = new SuperChecker(exchange);
        } catch (IllegalArgumentException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: specified invalid exchange name. See resourcesupport.Exchange for list of valid names.");
            System.exit(1);
        }
        SpringApplication.run(ExchangeServer.class, args);
        
		startSystem(exchangeServerName);
	}
	
	private static void initData(Hashtable<Stock, ArrayList<PriceInstance>> pricesTable, StockPriceRepository stockPriceRepository, Hashtable<Stock, IPO> quantitiesTable, StockQuantityRepository stockQuantityRepository) {
        System.out.println("Starting db init");
        
        Set<Stock> stockPricekeys = pricesTable.keySet();
		ArrayList<StockPrice> finalStockPrices = new ArrayList<>();
		ArrayList<StockQuantity> finalStockQuantities = new ArrayList<>();
        for(Stock key: stockPricekeys){

            IPO ipo = quantitiesTable.get(key);
            StockQuantity stockQuantity = new StockQuantity(key.name(), key.exchange().name(), ipo.getQuantity(), ipo.getStartTime());
            finalStockQuantities.add(stockQuantity);
        	
            ArrayList<PriceInstance> prices = pricesTable.get(key);
            for(PriceInstance price: prices){
            	StockPrice stockPrice = new StockPrice(key.name(), key.exchange().name(), price.price(), price.dateTime());
            	finalStockPrices.add(stockPrice);
            }
        }
        System.out.println("Starting quantities save");
        stockQuantityRepository.save(finalStockQuantities);

        System.out.println("Starting prices save");
        stockPriceRepository.save(finalStockPrices);
        
        System.out.println("Finished adding stock data to db!");
        
	}

	private static void startSystem(String exchangeServerName){

		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(exchange.portNum());
	    	while(!serverSocket.isClosed()){
				try {
		    		ClientConnection peerServer = new ClientConnection(serverSocket);
			        peerServer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	        System.out.println("Closing " + exchangeServerName + " exchange server!");
		    serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

}
