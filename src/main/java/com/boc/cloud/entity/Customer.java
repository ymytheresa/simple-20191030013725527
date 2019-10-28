package com.boc.cloud.entity;
public class Customer extends BaseEntity{
	private String customer_id;
	private String username;
	private String password;
	private String full_name;
	private String address1;
	private String address2;
	private String address3;
	private String phone_no;
	public String getAddress1(){
		return address1;
	}
	public String getAddress2(){
		return address2;
	}
	public String getAddress3(){
		return address3;
	}
	public String getCustomer_id(){
		return customer_id;
	}
	public String getFull_name(){
		return full_name;
	}
	public String getPassword(){
		return password;
	}
	public String getPhone_no(){
		return phone_no;
	}
	public String getUsername(){
		return username;
	}
	public void setAddress1(String address1){
		this.address1=address1;
	}
	public void setAddress2(String address2){
		this.address2=address2;
	}
	public void setAddress3(String address3){
		this.address3=address3;
	}
	public void setCustomer_id(String customer_id){
		this.customer_id=customer_id;
	}
	public void setFull_name(String full_name){
		this.full_name=full_name;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setPhone_no(String phone_no){
		this.phone_no=phone_no;
	}
	public void setUsername(String username){
		this.username=username;
	}
}
