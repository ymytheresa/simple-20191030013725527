package com.boc.cloud.entity;
public class Credit_Card_Txn extends BaseEntity{
	private String datetime;
	private String card_no;
	private String currency;
	private String amount;
	private String remark;
	public String getAmount(){
		return amount;
	}
	public String getCard_no(){
		return card_no;
	}
	public String getCurrency(){
		return currency;
	}
	public String getDatetime(){
		return datetime;
	}
	public String getRemark(){
		return remark;
	}
	public void setAmount(String amount){
		this.amount=amount;
	}
	public void setCard_no(String card_no){
		this.card_no=card_no;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setDatetime(String datetime){
		this.datetime=datetime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
