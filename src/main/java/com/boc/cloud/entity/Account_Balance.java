package com.boc.cloud.entity;
public class Account_Balance extends BaseEntity{
	private String account_id;
	private String currency;
	private String balance;
	public String getAccount_id(){
		return account_id;
	}
	public String getBalance(){
		return balance;
	}
	public String getCurrency(){
		return currency;
	}
	public void setAccount_id(String account_id){
		this.account_id=account_id;
	}
	public void setBalance(String balance){
		this.balance=balance;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
}
