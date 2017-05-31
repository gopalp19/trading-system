package com.mpcs.distributed.systems.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpcs.distributed.systems.ExchangeServer;
import com.mpcs.distributed.systems.model.StockPrice;
import com.mpcs.distributed.systems.model.StockQuantity;
import com.mpcs.distributed.systems.repositories.StockPriceRepository;
import com.mpcs.distributed.systems.repositories.StockQuantityRepository;

import messenger.*;
import resourcesupport.*;

@Service
public class StockService {
	private static ConcurrentHashMap<Stock, Integer> thresholds = new ConcurrentHashMap<>();

	@Autowired
    private StockPriceRepository stockPriceRepository;
	
	@Autowired
    private StockQuantityRepository stockQuantityRepository;

	public void buyStock(BuyMessage message, BuyResultMessage br) {
		synchronized (StockService.class) {
			System.out.println("Inside stock service buyStock");
			
			//LocalDateTime currentExchangeTime =  ExchangeServer.exchangeTimer.getTime().truncatedTo(ChronoUnit.HOURS);
			LocalDateTime currentExchangeTime = marketHours(ExchangeServer.exchangeTimer.getTime());
			
			List<StockPrice> stockPrices = stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime(message.stock.toString(), ExchangeServer.exchange.toString(), currentExchangeTime);
			
			if(stockPrices.isEmpty()){
				//Stock not in exchanges region
				System.out.println("Stock not in exchange's region");
			}else{
				//Stock exists 
				List<StockQuantity> stockQuantityList = stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), ExchangeServer.exchange.toString());
				
				if(!stockQuantityList.isEmpty()){

					StockQuantity stockQuantity = stockQuantityList.get(0);
					StockPrice stockPrice = stockPrices.get(0);
					
					if(stockQuantity.getIpoTime().isAfter(currentExchangeTime)){
						//IPO has not started and stocks do not exist
						br.notificationMessage = "IPO has not started, no stock exists!";
						return;
					}
					
					//Make sure amount of stocks in db is greater than how many user wants
					if(stockQuantity.getQuantity() - getThreshold(message.stock) >= message.quantity){
						//Update db to remove stocks
						int updatedStockQuantity = stockQuantity.getQuantity() - message.quantity;
						stockQuantity.setQuantity(updatedStockQuantity);
						stockQuantityRepository.save(stockQuantity);
						
	        			br.quantityBought = message.quantity;
	        			br.totalPrice = stockPrice.getPrice() * message.quantity;
	        			System.out.println("new qty of " + message.stock + " is " + updatedStockQuantity);
	        			ExchangeServer.logger.messageQueue.add(br);
						//Return success, user bough stocks
					}else{
						//Quantity too large...give error message
						br.notificationMessage = "Quantity demanded is greater than current stock supply!";
					}
				}else{
					//Stock is not found in quantities table...this should never happen at this point
					System.out.println("Stock not found in qty table");
				}
			}
		}
	}

	public void sellStock(SellMessage message, SellResultMessage sr) {
		synchronized(StockService.class) {
			System.out.println("Inside stock service sellStock");
			LocalDateTime currentExchangeTime = marketHours(ExchangeServer.exchangeTimer.getTime());
			
			List<StockPrice> stockPrices = stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime(message.stock.toString(), ExchangeServer.exchange.toString(), currentExchangeTime);
			
			if(stockPrices.isEmpty()){
				//Stock not in exchanges region
				System.out.println("Stock not in exchange's region");
			} else {
				//Stock exists 
				List<StockQuantity> stockQuantityList = stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), ExchangeServer.exchange.toString());
				
				if(!stockQuantityList.isEmpty()){

					StockQuantity stockQuantity = stockQuantityList.get(0);
					StockPrice stockPrice= stockPrices.get(0);
					
					//Update db to insert stocks
					int updatedStockQuantity = stockQuantity.getQuantity() + message.quantity;
					stockQuantity.setQuantity(updatedStockQuantity);
					stockQuantityRepository.save(stockQuantity);
					
	    			sr.quantitySold = message.quantity;
	    			sr.totalPrice = stockPrice.getPrice() * message.quantity;
	    			System.out.println("new qty of " + message.stock + " is " + updatedStockQuantity);
	    			ExchangeServer.logger.messageQueue.add(sr);
						
	    			//Return success, user sold stocks
				}else{
					//Stock is not found in quantities table...this should never happen at this point
					System.out.println("Stock not found in qty table");
				}
			}
		}
	}
	
	/**
	 * @return 0 if failed, else return quantity successfully reserved
	 */
	public int reserveStock(Stock stock, int quantity) {
		System.out.println("Inside stock service reserveStock");
		LocalDateTime currentExchangeTime = marketHours(ExchangeServer.exchangeTimer.getTime());

		List<StockPrice> stockPrices = stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime(stock.toString(), stock.exchange().toString(), currentExchangeTime);
		
		if(stockPrices.isEmpty()){
			//Stock not in exchanges region
		}else{
			//Stock exists 
			List<StockQuantity> stockQuantityList = stockQuantityRepository.findByStockNameAndExchangeName(stock.toString(), stock.exchange().toString());
			
			if(!stockQuantityList.isEmpty()){

				StockQuantity stockQuantity = stockQuantityList.get(0);
				
				//Make sure amount of stocks in db is greater than how many user wants
				if(stockQuantity.getQuantity() - getThreshold(stock) >= quantity){
					int old = getThreshold(stock);
					thresholds.put(stock, old + quantity);
					return quantity;
					//Return success, user reserved stocks
				}
			}else{
				//Stock is not found in quantities table...this should never happen at this point
			}
		}
		return 0;
	}

	/**
	 * unreserve some stock
	 */
	public void unreserveStock(Stock stock, int quantity) {
		synchronized (StockService.class) {
			System.out.println("Inside stock service unreserveStock");
			int old = getThreshold(stock);
			thresholds.put(stock,  old - quantity);			
		}
	}
	
	/**
	 * @return current reserved qty of this stock
	 */
	private static int getThreshold(Stock stock) {
		if (thresholds.containsKey(stock)) {
			return thresholds.get(stock);
		} else {
			thresholds.put(stock, 0);
			return 0;
		}
	}
	
	private static LocalDateTime marketHours(LocalDateTime old) {
		if (old == null) return null;
		switch (old.getDayOfWeek()) {
			case SATURDAY:
				return old.withDayOfYear(old.getDayOfYear()-1).withHour(16);
			case SUNDAY:
				return old.withDayOfYear(old.getDayOfYear()-2).withHour(16);
			default:
				if (old.getHour() < 8) {
					return marketHours(old.withDayOfYear(old.getDayOfYear()-1).withHour(16));
				} else if (old.getHour() > 16) {
					return old.withHour(16);
				}
		}
		return old;
	}
}
