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

import messenger.SellMessage;
import messenger.SellResultMessage;
import resourcesupport.Exchange;
import resourcesupport.Stock;

@RunWith(MockitoJUnitRunner.class)
public class SellTests {
	
	@InjectMocks
	StockService stockService;
	
	@Mock
	private StockPriceRepository stockPriceRepository;
	
	@Mock
	private StockQuantityRepository stockQuantityRepository;
	
	SellMessage message;
	SellResultMessage result;
	
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
		
		stockService.sellStock(message, result);
		
		assertEquals(result.quantitySold, (Integer) 0);
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
		
		stockService.sellStock(message, result);
		
		assertEquals(result.quantitySold, (Integer) 0);
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
		
		stockService.sellStock(message, result);
		
		assertEquals(result.quantitySold, message.quantity);
	}


	public void setupMessages(){
		message = new SellMessage(Exchange.NEW_YORK_STOCK_EXCHANGE, "test", Stock.IBM, 10, null, null);
		result = new SellResultMessage(Exchange.NEW_YORK_STOCK_EXCHANGE, "test", Stock.IBM, (Integer) 0, (float) 0, null);
	}
}
