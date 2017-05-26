package com.mpcs.distributed.systems.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StockPrice {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    private String stockName;
    private String exchangeName;
    private float price;
    private LocalDateTime systemDateTime;
    
    protected StockPrice(){}
    
    public StockPrice(String stockName, String exchangeName, float price, LocalDateTime systemDateTime){
    	this.stockName = stockName;
    	this.exchangeName = exchangeName;
    	this.price = price;
    	this.systemDateTime = systemDateTime;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public LocalDateTime getSystemDateTime() {
		return systemDateTime;
	}

	public void setSystemDateTime(LocalDateTime systemDateTime) {
		this.systemDateTime = systemDateTime;
	}

}
