package com.mpcs.distributed.systems.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mpcs.distributed.systems.model.StockPrice;

@Repository
public interface StockPriceRepository extends CrudRepository<StockPrice, Long> {
	
	List<StockPrice> findByStockNameAndExchangeNameAndSystemDateTime(String stockName, String exchangeName, LocalDateTime systemDateTime);

}
