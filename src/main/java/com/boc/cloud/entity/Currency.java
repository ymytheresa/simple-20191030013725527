package com.boc.cloud.entity;
public class Currency extends BaseEntity{
	private String currency_code;
	private String description;
	public String getCurrency_code(){
		return currency_code;
	}
	public String getDescription(){
		return description;
	}
	public void setCurrency_code(String currency_code){
		this.currency_code=currency_code;
	}
	public void setDescription(String description){
		this.description=description;
	}
}
