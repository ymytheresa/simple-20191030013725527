package com.boc.cloud.api.rest.bankinfo;

import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.entity.Atm;
import com.boc.cloud.entity.Branch;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * List bank branches & List ATMs
 *
 * @author wuxi
 */
@RestController
public class BankInfoApi extends BaseRestApi {
    private List<String> businessStatuses = Arrays.asList("UNCONGESTED", "CONGESTED", "VERY_CONGESTED");
    private Random rand = new Random();

    private Double centralLat = 22.28;
    private Double centralLong = 114.1588;
    private Double tsuenwanLat = 22.3699;
    private Double tsuenwanLong = 114.1144;
    private Double shatinLat = 22.3771;
    private Double shatinLong = 114.1974;
    private Double threshold = 0.06;

    @ApiOperation(value = "Get a list of bank branches", notes = "Get a list of bank branches", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/bank-info/branches", method = RequestMethod.GET)
    public List<Branch> queryBranch(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Branch branch = new Branch();

        List<Branch> branchList = db.findByIndex(getSelector(branch), Branch.class);
        branchList.forEach(it -> {
            int randomNo = rand.nextInt(10);
            int mappedValue;

            // non-office hour
            if ((LocalTime.now().isBefore(LocalTime.of(9, 0)) || LocalTime.now().isAfter(LocalTime.of(17, 0)))) {
                mappedValue = 0; // UNCONGESTED
            } else {
                if (Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - centralLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - centralLong, 2.0)) < threshold
                    || Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - tsuenwanLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - tsuenwanLong, 2.0)) < threshold
                    || Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - shatinLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - shatinLong, 2.0)) < threshold) {
                    // higher chance to be congested or very congested
                    if (randomNo >= 7)
                        mappedValue = 2;  // VERY_CONGESTED
                    else if (randomNo >= 3)
                        mappedValue = 1;  // CONGESTED
                    else
                        mappedValue = 0; // UNCONGESTED
                } else {
                    // higher chance to be uncongested
                    if (randomNo >= 6)
                        mappedValue = 0;  // UNCONGESTED
                    else if (randomNo >= 3)
                        mappedValue = 1;  // CONGESTED
                    else
                        mappedValue = 2; // VERY_CONGESTED
                }
            }
            it.setBusiness_status(businessStatuses.get(mappedValue));
        });

        return branchList;
    }

    @ApiOperation(value = "Get a list of ATMs", notes = "Get a list of ATMs", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/bank-info/atms", method = RequestMethod.GET)
    public List<Atm> queryATM(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Atm atm = new Atm();

        List<Atm> atmList = db.findByIndex(getSelector(atm), Atm.class);
        atmList.forEach(it -> it.setBusiness_status(businessStatuses.get(rand.nextInt(businessStatuses.size()))));
        atmList.forEach(it -> {
            int randomNo = rand.nextInt(10);
            int mappedValue;

            // non-busy hour
            if ((LocalTime.now().isBefore(LocalTime.of(10, 0)) || LocalTime.now().isAfter(LocalTime.of(20, 0)))) {
                mappedValue = 0; // UNCONGESTED
            } else {
                if (Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - centralLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - centralLong, 2.0)) < threshold
                    || Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - tsuenwanLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - tsuenwanLong, 2.0)) < threshold
                    || Math.sqrt(Math.pow(it.getAddress_coordinates().getLatitude() - shatinLat, 2.0) + Math.pow(it.getAddress_coordinates().getLongitude() - shatinLong, 2.0)) < threshold) {
                    // higher chance to be congested or very congested
                    if (randomNo >= 7)
                        mappedValue = 2;  // VERY_CONGESTED
                    else if (randomNo >= 3)
                        mappedValue = 1;  // CONGESTED
                    else
                        mappedValue = 0; // UNCONGESTED
                } else {
                    // higher chance to be uncongested
                    if (randomNo >= 6)
                        mappedValue = 0;  // UNCONGESTED
                    else if (randomNo >= 3)
                        mappedValue = 1;  // CONGESTED
                    else
                        mappedValue = 2; // VERY_CONGESTED
                }
            }
            it.setBusiness_status(businessStatuses.get(mappedValue));
        });

        return atmList;
    }
}
