package com.boc.cloud.api.rest.customer;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.entity.Customer;
import com.boc.cloud.model.response.CustomerProfile;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuxi
 * Get customer profile
 */
@RestController
public class ProfileApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Get the customer account information", notes = "Get the customer account information", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/customer/profile", method = RequestMethod.GET)
    public CustomerProfile getCustomerInfo(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Customer obj = new Customer();
        String CustomerId = getCustomerId(request);
        checkService.checkCustomerId(CustomerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        obj.setCustomer_id(CustomerId);

        Customer info = db.findByIndex(getSelector(obj), Customer.class).get(0);

        CustomerProfile output = new CustomerProfile();
        output.setAddress1(info.getAddress1());
        output.setAddress2(info.getAddress2());
        output.setAddress3(info.getAddress3());
        output.setFull_name(info.getFull_name());
        output.setPhone_no(info.getPhone_no());
        return output;
    }

}
