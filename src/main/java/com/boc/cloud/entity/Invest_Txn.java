package com.boc.cloud.entity;
public class Invest_Txn extends BaseEntity{
	private String date_time;
	private String account_id;
	private String action;
	private String currency;
	private String code;
	private String price;
	private String quantity;
	private String amount;
	private String pe_ratio;
	private String remark;
	public String getAccount_id(){
		return account_id;
	}
	public String getAction(){
		return action;
	}
	public String getAmount(){
		return amount;
	}
	public String getCode(){
		return code;
	}
	public String getCurrency(){
		return currency;
	}
	public String getDate_time(){
		return date_time;
	}
	public String getPe_ratio(){
		return pe_ratio;
	}
	public String getPrice(){
		return price;
	}
	public String getQuantity(){
		return quantity;
	}
	public String getRemark(){
		return remark;
	}
	public void setAccount_id(String account_id){
		this.account_id=account_id;
	}
	public void setAction(String action){
		this.action=action;
	}
	public void setAmount(String amount){
		this.amount=amount;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setDate_time(String date_time){
		this.date_time=date_time;
	}
	public void setPe_ratio(String pe_ratio){
		this.pe_ratio=pe_ratio;
	}
	public void setPrice(String price){
		this.price=price;
	}
	public void setQuantity(String quantity){
		this.quantity=quantity;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
