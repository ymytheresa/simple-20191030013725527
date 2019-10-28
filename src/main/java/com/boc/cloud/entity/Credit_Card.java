package com.boc.cloud.entity;
public class Credit_Card extends BaseEntity{
	private String card_id;
	private String card_no;
	private String customer_id;
	private String card_type;
	private String open_date;
	private String currency;
	private String credit_limit;
	private String credit_used;
	public String getCard_id(){
		return card_id;
	}
	public String getCard_no(){
		return card_no;
	}
	public String getCredit_limit(){
		return credit_limit;
	}
	public String getCredit_used(){
		return credit_used;
	}
	public String getCurrency(){
		return currency;
	}
	public String getCustomer_id(){
		return customer_id;
	}
	public String getOpen_date(){
		return open_date;
	}

	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public void setCard_id(String card_id){
		this.card_id=card_id;
	}
	public void setCard_no(String card_no){
		this.card_no=card_no;
	}
	public void setCredit_limit(String credit_limit){
		this.credit_limit=credit_limit;
	}
	public void setCredit_used(String credit_used){
		this.credit_used=credit_used;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setCustomer_id(String customer_id){
		this.customer_id=customer_id;
	}
	public void setOpen_date(String open_date){
		this.open_date=open_date;
	}
}
