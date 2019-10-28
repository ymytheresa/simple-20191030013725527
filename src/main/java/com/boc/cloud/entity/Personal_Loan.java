package com.boc.cloud.entity;

public class Personal_Loan extends BaseEntity {

	private String account_id;
	private String open_date;
    private String original_amount;
	private String currency;
	private String amount;
	private String no_of_terms;
	private String rate;
	private String installment_amount;
	private String installment_day;
	private String remark;
	public String getAccount_id(){
		return account_id;
	}
	public String getAmount(){
		return amount;
	}
	public String getCurrency(){
		return currency;
	}
	public String getInstallment_amount(){
		return installment_amount;
	}
	public String getInstallment_day(){
		return installment_day;
	}
	public String getNo_of_terms(){
		return no_of_terms;
	}
	public String getOpen_date(){
		return open_date;
	}
	public String getOriginal_amount(){
		return original_amount;
	}
	public String getRate(){
		return rate;
	}
	public String getRemark(){
		return remark;
	}
	public void setAccount_id(String account_id){
		this.account_id=account_id;
	}
	public void setAmount(String amount){
		this.amount=amount;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setInstallment_amount(String installment_amount){
		this.installment_amount=installment_amount;
	}
	public void setInstallment_day(String installment_day){
		this.installment_day=installment_day;
	}
	public void setNo_of_terms(String no_of_terms){
		this.no_of_terms=no_of_terms;
	}
	public void setOpen_date(String open_date){
		this.open_date=open_date;
	}
	public void setOriginal_amount(String original_amount){
		this.original_amount=original_amount;
	}
	public void setRate(String rate){
		this.rate=rate;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
