package com.boc.cloud.entity;
public class Account extends BaseEntity{
	private String account_id;
	private String account_no;
	private String customer_id;
	public String getAccount_id(){
		return account_id;
	}
	public String getAccount_no(){
		return account_no;
	}
	public String getCustomer_id(){
		return customer_id;
	}
	public void setAccount_id(String account_id){
		this.account_id=account_id;
	}
	public void setAccount_no(String account_no){
		this.account_no=account_no;
	}
	public void setCustomer_id(String customer_id){
		this.customer_id=customer_id;
	}
}
