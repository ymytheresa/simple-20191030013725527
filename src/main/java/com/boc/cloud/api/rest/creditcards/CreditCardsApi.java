package com.boc.cloud.api.rest.creditcards;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.exception.Http404Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.Credit_Card;
import com.boc.cloud.entity.Credit_Card_Txn;
import com.boc.cloud.model.request.CreditCardPaymentRequest;
import com.boc.cloud.model.response.CreditCard;
import com.boc.cloud.model.response.CreditCardPaymentResponse;
import com.boc.cloud.model.response.CreditCardTransaction;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * Credit Card
 *
 * @author wangmengxue
 */
@RestController
public class CreditCardsApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "List the credit cards", notes = "List the credit cards owned by the customer", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/credit-cards", method = RequestMethod.GET)
    public List<CreditCard> getCreditCardsByAccountId(HttpServletRequest request) throws Exception {
        // FIXME: 从公共方法中得到customer_id并需要判断customer_id是否为空
        String customer_id = this.getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        Database db = getDb(request);
        if (customer_id == null) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CUSTOMER_ID, MsgKeys.NOT_EXIST, "Customer ID");
        }

        Credit_Card card = new Credit_Card();
        card.setCustomer_id(customer_id);
        return db.findByIndex(getSelector(card), CreditCard.class);
    }

    @ApiOperation(value = "List credit card transaction records", notes = "List out all transactions involved with the specified credit card", produces = "application/json")
    @ApiImplicitParam(name = "card_no", value = "card_no", defaultValue = "5228650000008331", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "creditCardNotFound", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/credit-cards/{card_no}/transactions", method = RequestMethod.GET)
    public List<CreditCardTransaction> getCreditCardByCardId(@PathVariable String card_no,
                                                             HttpServletRequest request)
        throws Exception {
        // FIXME: 从公共方法中得到customer_id并需要判断customer_id是否为空
        String customer_id = this.getCustomerId(request);
        Database db = getDb(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);

        Credit_Card card = new Credit_Card();
        card.setCustomer_id(customer_id);
        card.setCard_no(card_no);

        List<Credit_Card> cards = db.findByIndex(getSelector(card), Credit_Card.class);
        if (cards == null || cards.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_CREDIT_CARD_NOT_FOUND, MsgKeys.NOT_EXIST, "Credit card");
        }

        card_no = cards.get(0).getCard_no();

        Credit_Card_Txn credit_card_tnx = new Credit_Card_Txn();
        credit_card_tnx.setCard_no(card_no);
        List<CreditCardTransaction> txns = db.findByIndex(getSelector(credit_card_tnx), CreditCardTransaction.class);
        txns.sort(Comparator.comparing(CreditCardTransaction::getDatetime).reversed());
        return txns;
    }

    /**
     * 信用卡消費.
     *
     * @param input
     * @return
     * @throws Exception
     */
    // FIXME : 没有样式
    @ApiOperation(value = "Perform credit card payment", notes = "Pay the merchant using the specified credit card. Assume the merchant (i.e. you) has already registered an account in the bank to receive payment.", consumes = "application/json", produces = "application/json")
    @ApiImplicitParam(name = "card_no", value = "card_no", defaultValue = "5228650000008331", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "amountRequired / currencyRequired / invalidAmount / " +
            "creditCardNotFound / invalidCurrency / insufficientCredit",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/credit-cards/{card_no}/payment", method = RequestMethod.POST)
    public CreditCardPaymentResponse payment(@PathVariable String card_no,
                                             @RequestBody @ApiParam(value = "credit card payment input values(JSON)") CreditCardPaymentRequest input,
                                             HttpServletRequest request) throws Exception {
        // FIXME: 文档没有描述此API，按照自己的理解写的
        // FIXME: 从公共方法中得到customer_id并需要判断customer_id是否为空
        Double used_amount;
        Double credit_card_limit;
        String customer_id = this.getCustomerId(request);
        Database db = getDb(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        if (customer_id == null) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CUSTOMER_ID, MsgKeys.NOT_EXIST, "customer_id");
        }
        if (String.valueOf(input.getAmount()).equals("") || input.getAmount() == null) {
            throw new Http400Exception(MsgKeys.ERROR_AMOUNT_REQUIRED, MsgKeys.INPUT_REQUIRED, "amount");
        }
        if ("".equals(input.getCurrency()) || input.getCurrency() == null) {
            throw new Http400Exception(MsgKeys.ERROR_CURRENCY_REQUIRED, MsgKeys.INPUT_REQUIRED, "currency");
        }
        if (input.getAmount() <= 0) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GT, "amount ", "0");
        }

        Credit_Card card = new Credit_Card();
        card.setCustomer_id(customer_id);
        card.setCard_no(card_no);
        List<Credit_Card> cards = db.findByIndex(getSelector(card), Credit_Card.class);
        if (cards == null || cards.size() != 1) {
            throw new Http404Exception(MsgKeys.ERROR_CREDIT_CARD_NOT_FOUND, MsgKeys.NOT_EXIST, "Credit card");
        }
        card_no = cards.get(0).getCard_no();
        if (!cards.get(0).getCurrency().equals(input.getCurrency())) {
            throw new Http404Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.CREDIT_CARD_INVALID_CURRENCY, cards.get(0).getCurrency(), input.getCurrency());
        }

        credit_card_limit = Double.parseDouble(cards.get(0).getCredit_limit());
        used_amount = Common.add(Double.parseDouble(cards.get(0).getCredit_used()), input.getAmount());
        // two decimal places
        BigDecimal b = new BigDecimal(used_amount);
        used_amount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        if (credit_card_limit < used_amount) {
            throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_CREDIT, MsgKeys.CREDIT_CARD_AMT_INSUFFICIENT);
        }

        cards.get(0).setCredit_used(String.valueOf(used_amount));
        db.update(cards.get(0));

        Credit_Card_Txn credit_card_tnx = new Credit_Card_Txn();
        credit_card_tnx.setCard_no(card_no);
        credit_card_tnx.setAmount("-" + input.getAmount());
        credit_card_tnx.setCurrency(input.getCurrency());
        credit_card_tnx.setDatetime(now());
        credit_card_tnx.setRemark(input.getRemark());
        credit_card_tnx.setType("Credit_Card_Txn");
        db.save(credit_card_tnx);

        CreditCardPaymentResponse result = new CreditCardPaymentResponse();
        result.setCredit_unused(credit_card_limit - used_amount);
        return result;
    }
}
