package com.boc.cloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Stock extends BaseEntity {

    @ApiModelProperty(value = "Stock code", required = true, example = "2388")
    private String stock_code;

    @ApiModelProperty(value = "English name", required = true, example = "BOC HONG KONG")
    private String name_en;

    @ApiModelProperty(value = "Chinese name", required = true, example = "中銀香港")
    private String name_tc;

    @ApiModelProperty(value = "Price", required = true, example = "38.8")
    private String price;

    @ApiModelProperty(value = "Change", required = true, example = "-0.05")
    private String change;

    @ApiModelProperty(value = "Percentage change", required = true, example = "-0.13")
    private String change_pct;

    @ApiModelProperty(value = "P/E ratio", required = true, example = "7.39")
    private String pe_ratio;

    @ApiModelProperty(value = "Turnover volume", required = true, example = "309453000")
    private String turnover;

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getChange() {
        return change;
    }

    public String getChange_pct() {
        return change_pct;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_tc() {
        return name_tc;
    }

    public String getPe_ratio() {
        return pe_ratio;
    }

    public String getPrice() {
        return price;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setChange_pct(String change_pct) {
        this.change_pct = change_pct;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setName_tc(String name_tc) {
        this.name_tc = name_tc;
    }

    public void setPe_ratio(String pe_ratio) {
        this.pe_ratio = pe_ratio;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

}
