package com.mpcs.distributed.systems.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mpcs.distributed.systems.model.StockQuantity;

@Repository
public interface StockQuantityRepository extends CrudRepository<StockQuantity, Long> {

	List<StockQuantity> findByStockNameAndExchangeName(String stockName, String exchangeName);

}
