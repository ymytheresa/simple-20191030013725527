package com.boc.cloud.api.rest.marketinfo;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.entity.FX_Rate;
import com.boc.cloud.entity.Stock;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wang meng xue
 */
@RestController
public class MarketInfoApi extends BaseRestApi {

    /**
     * Query currency exchange rate
     */
    @ApiOperation(value = "Get the rate of currency conversion", notes = "Get the rate of currency conversion（The 100 - bit conversion standard of Hong Kong dollar）", produces = "application/json")
    @ApiImplicitParam(paramType = "query", name = "currency", value = "currency", required = true, defaultValue = "HKD")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "invalidCurrency", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/market-info/fx-rate", method = RequestMethod.GET)
    public FX_Rate queryCurrencyRate(@RequestParam(required = true) String currency, HttpServletRequest request)
        throws Exception {
        Database db = getDb(request);
        FX_Rate obj = new FX_Rate();
        obj.setCurrency(currency);

        //input currency不能为“HKD”
        if (currency.equals("HKD")) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.TX_EXCHANGE_CUR_MUST_NOT_HKD, "Currency");
        }
        List<FX_Rate> rates = db.findByIndex(getSelector(obj), FX_Rate.class);
        if (rates == null || rates.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.NOT_EXIST, "Currency");
        }

        return rates.get(0);
    }

    /**
     * 1202 查詢-股票價格 searching in table of blue_chips
     *
     * @param stockCode is code of stock
     * @return Blue_Chips must include stockCode, nameEng, nameCht, price, change,
     * changePct, peRatio, turnover
     */
    @ApiOperation(value = "Query stock price", notes = "Query stock price according to stock_code", produces = "application/json")
    @ApiImplicitParam(paramType = "query", name = "stock_code", value = "stock_code", required = true, defaultValue = "1")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "stockNotFound", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/market-info/stock", method = RequestMethod.GET)
    public Stock getStockPriceBycode(@RequestParam(required = true, name = "stock_code") String stockCode,
                                     HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Stock obj = new Stock();
        obj.setStock_code(String.valueOf(Integer.parseInt(stockCode)));
        List<Stock> stocks = db.findByIndex(getSelector(obj), Stock.class);
        if (stocks == null || stocks.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_STOCK_NOT_FOUND, MsgKeys.NOT_EXIST, "Stock");
        }
        return stocks.get(0);
    }
}
