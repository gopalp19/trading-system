package com.mpcs.distributed.systems.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StockQuantity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    private String stockName;
    private String exchangeName;
    private int quantity;
    private LocalDateTime ipoTime;
    
    protected StockQuantity(){}
    
    public StockQuantity(String stockName, String exchangeName, int quantity, LocalDateTime ipoTime){
    	this.stockName = stockName;
    	this.exchangeName = exchangeName;
    	this.quantity = quantity;
    	this.ipoTime = ipoTime;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getIpoTime() {
		return ipoTime;
	}

	public void setIpoTime(LocalDateTime ipoTime) {
		this.ipoTime = ipoTime;
	}
}
