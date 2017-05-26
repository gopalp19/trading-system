package com.mpcs.distributed.systems.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpcs.distributed.systems.ExchangeServer;
import com.mpcs.distributed.systems.model.StockPrice;
import com.mpcs.distributed.systems.model.StockQuantity;
import com.mpcs.distributed.systems.repositories.StockPriceRepository;
import com.mpcs.distributed.systems.repositories.StockQuantityRepository;

import messenger.BuyMessage;
import messenger.BuyResultMessage;

@Service
public class StockService {

	@Autowired
    private StockPriceRepository stockPriceRepository;
	
	@Autowired
    private StockQuantityRepository stockQuantityRepository;

	public void buyStock(BuyMessage message, BuyResultMessage br) {
		LocalDateTime test = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 5);
		LocalDateTime newTest =  test.truncatedTo(ChronoUnit.HOURS);
		
		List<StockPrice> stockPrices = stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime(message.stock.toString(), ExchangeServer.exchange.toString(), newTest);
		
		if(stockPrices.isEmpty()){
			//Stock not in exchanges region
		}else{
			//Stock exists 
			List<StockQuantity> stockQuantityList = stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), ExchangeServer.exchange.toString());
			
			if(!stockQuantityList.isEmpty()){

				StockQuantity stockQuantity = stockQuantityList.get(0);
				StockPrice stockPrice= stockPrices.get(0);
				
				//Make sure amount of stocks in db is greater than how many user wants
				if(stockQuantity.getQuantity() > message.quantity){
					//Update db to remove stocks
					int updatedStockQuantity = stockQuantity.getQuantity() - message.quantity;
					stockQuantity.setQuantity(updatedStockQuantity);
					stockQuantityRepository.save(stockQuantity);
					
        			br.quantityBought = message.quantity;
        			br.totalPrice = stockPrice.getPrice() * message.quantity;
					
					//Return success, user bough stocks
				}
			}else{
				//Stock is not found in quantities table...this should never happen at this point
			}
		}
	}
}
