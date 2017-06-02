import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.mpcs.distributed.systems.ExchangeServer;
import com.mpcs.distributed.systems.Logger;
import com.mpcs.distributed.systems.model.StockPrice;
import com.mpcs.distributed.systems.model.StockQuantity;
import com.mpcs.distributed.systems.repositories.StockPriceRepository;
import com.mpcs.distributed.systems.repositories.StockQuantityRepository;
import com.mpcs.distributed.systems.services.StockService;

import messenger.BuyMessage;
import messenger.BuyResultMessage;
import resourcesupport.Exchange;
import resourcesupport.Stock;

@RunWith(MockitoJUnitRunner.class)
public class BuyTests {
	
	@InjectMocks
	StockService stockService;
	
	@Mock
	private StockPriceRepository stockPriceRepository;
	
	@Mock
	private StockQuantityRepository stockQuantityRepository;
	
	BuyMessage message;
	BuyResultMessage result;
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
		setupMessages();
    }
 
	@Test
	public void wrongExchangetest() {
		
		ExchangeServer.exchange = Exchange.NEW_YORK_STOCK_EXCHANGE;
		
		List<StockPrice> stockPrices = new ArrayList<>();
		LocalDateTime time = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 0);
		
		when(stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime("IBM", 
				"NEW_YORK_STOCK_EXCHANGE", time)).thenReturn(stockPrices);
		
		stockService.buyStock(message, result);
		
		assertEquals(result.quantityBought, (Integer) 0);
	}
	
	@Test
	public void invalidStockQuantitytest() {
		
		ExchangeServer.exchange = Exchange.NEW_YORK_STOCK_EXCHANGE;
		
		List<StockPrice> stockPrices = new ArrayList<>();
		List<StockQuantity> stockQuantities = new ArrayList<>();
		LocalDateTime time = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 0);
		StockPrice price = new StockPrice("IBM", "NEW_YORK_STOCK_EXCHANGE", (float) 112.50, time);
		stockPrices.add(price);
		
		when(stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime("IBM", 
				"NEW_YORK_STOCK_EXCHANGE", time)).thenReturn(stockPrices);
		
		when(stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), 
				ExchangeServer.exchange.toString())).thenReturn(stockQuantities);
		
		stockService.buyStock(message, result);
		
		assertEquals(result.quantityBought, (Integer) 0);
	}
	
	@Test
	public void futureIPOtest() {
		
		ExchangeServer.exchange = Exchange.NEW_YORK_STOCK_EXCHANGE;
		
		List<StockPrice> stockPrices = new ArrayList<>();
		List<StockQuantity> stockQuantities = new ArrayList<>();
		LocalDateTime time = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 0);
		LocalDateTime laterIPOTime = LocalDateTime.of(2016, Month.JANUARY, 2, 9, 0);
		StockPrice price = new StockPrice("IBM", "NEW_YORK_STOCK_EXCHANGE", (float) 112.50, time);
		StockQuantity quantity = new StockQuantity("IBM", "NEW_YORK_STOCK_EXCHANGE", 500, laterIPOTime);
		stockPrices.add(price);
		stockQuantities.add(quantity);
		
		when(stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime("IBM", 
				"NEW_YORK_STOCK_EXCHANGE", time)).thenReturn(stockPrices);
		
		when(stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), 
				ExchangeServer.exchange.toString())).thenReturn(stockQuantities);
		
		stockService.buyStock(message, result);
		
		assertEquals(result.quantityBought, (Integer) 0);
		assertEquals(result.notificationMessage, "IPO has not started, no stock exists!");
	}
	
	@Test
	public void lowStockSupplytest() {
		
		ExchangeServer.exchange = Exchange.NEW_YORK_STOCK_EXCHANGE;
		
		List<StockPrice> stockPrices = new ArrayList<>();
		List<StockQuantity> stockQuantities = new ArrayList<>();
		LocalDateTime time = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 0);
		StockPrice price = new StockPrice("IBM", "NEW_YORK_STOCK_EXCHANGE", (float) 112.50, time);
		StockQuantity quantity = new StockQuantity("IBM", "NEW_YORK_STOCK_EXCHANGE", 500, time);
		stockPrices.add(price);
		stockQuantities.add(quantity);
		
		when(stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime("IBM", 
				"NEW_YORK_STOCK_EXCHANGE", time)).thenReturn(stockPrices);
		
		when(stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), 
				ExchangeServer.exchange.toString())).thenReturn(stockQuantities);
		
		message.quantity = 1000;
		stockService.buyStock(message, result);
		
		assertEquals(result.quantityBought, (Integer) 0);
		assertEquals(result.notificationMessage, "Quantity demanded is greater than current stock supply!");
	}
	
	@Test
	public void availableStocktest() {
		
		ExchangeServer.exchange = Exchange.NEW_YORK_STOCK_EXCHANGE;
		ExchangeServer.logger = new Logger();
		
		List<StockPrice> stockPrices = new ArrayList<>();
		List<StockQuantity> stockQuantities = new ArrayList<>();
		LocalDateTime time = LocalDateTime.of(2016, Month.JANUARY, 1, 9, 0);
		StockPrice price = new StockPrice("IBM", "NEW_YORK_STOCK_EXCHANGE", (float) 112.50, time);
		StockQuantity quantity = new StockQuantity("IBM", "NEW_YORK_STOCK_EXCHANGE", 500, time);
		stockPrices.add(price);
		stockQuantities.add(quantity);
		
		when(stockPriceRepository.findByStockNameAndExchangeNameAndSystemDateTime("IBM", 
				"NEW_YORK_STOCK_EXCHANGE", time)).thenReturn(stockPrices);
		
		when(stockQuantityRepository.findByStockNameAndExchangeName(message.stock.toString(), 
				ExchangeServer.exchange.toString())).thenReturn(stockQuantities);
		
		stockService.buyStock(message, result);
		
		assertEquals(result.quantityBought, message.quantity);
	}
	
	public void setupMessages(){
		message = new BuyMessage(Exchange.NEW_YORK_STOCK_EXCHANGE, "test", Stock.IBM, 10, null, null);
		result = new BuyResultMessage(Exchange.NEW_YORK_STOCK_EXCHANGE, "test", Stock.IBM, (Integer) 0, (float) 0, null);
	}

}
