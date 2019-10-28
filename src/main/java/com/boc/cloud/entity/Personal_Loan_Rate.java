package com.boc.cloud.entity;
public class Personal_Loan_Rate extends BaseEntity{
	private String amount_cap;
	private String annual_interest_rate;
	private String currency;
	public String getAmount_cap(){
		return amount_cap;
	}
	public String getAnnual_interest_rate(){
		return annual_interest_rate;
	}
	public void setAmount_cap(String amount_cap){
		this.amount_cap=amount_cap;
	}
	public void setAnnual_interest_rate(String annual_interest_rate){
		this.annual_interest_rate=annual_interest_rate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
