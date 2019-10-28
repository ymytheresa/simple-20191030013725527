package com.boc.cloud.entity;
public class Third_Party_Account extends BaseEntity{
	private String fps_phone_no;
	private String fps_email;
	private String fps_id;
	private String account_name;
	public String getAccount_name(){
		return account_name;
	}
	public String getFps_email(){
		return fps_email;
	}
	public String getFps_id(){
		return fps_id;
	}
	public String getFps_phone_no(){
		return fps_phone_no;
	}
	public void setAccount_name(String account_name){
		this.account_name=account_name;
	}
	public void setFps_email(String fps_email){
		this.fps_email=fps_email;
	}
	public void setFps_id(String fps_id){
		this.fps_id=fps_id;
	}
	public void setFps_phone_no(String fps_phone_no){
		this.fps_phone_no =fps_phone_no;
	}
}
