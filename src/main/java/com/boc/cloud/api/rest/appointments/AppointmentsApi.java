package com.boc.cloud.api.rest.appointments;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.exception.Http404Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.model.request.AppointmentCreationRequest;
import com.boc.cloud.model.response.Appointment;
import com.boc.cloud.model.response.AppointmentCancelResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Appointment
 *
 * @author wangmengxue
 */
@RestController
public class AppointmentsApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    private String[] AppointmentRange = new String[]{"WEALTH_MANAGEMENT", "PRIVATE_BANKING",
        "CORP_BANKING", "RMB_SERVICES", "CROSS_BORDER_SERVICES", "INVESTMENT", "MORTGAGE", "LOAN", "OTHERS"};

    @ApiOperation(value = "Get customer appointment list", notes = "Get the customer appointment list from phone number", produces = "application/json")
    @ApiImplicitParam(name = "phone_no", value = "phone_no", defaultValue = "61234567", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "invalidPhoneNo", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/services/appointments/{phone_no}", method = RequestMethod.GET)
    public List<Appointment> getAppoinmentsByAccountId(@PathVariable String phone_no,
                                                       HttpServletRequest request) throws Exception {
        Database db = getDb(request);

        String phone = phone_no.replaceAll("\\-", "");
        Pattern pattern = Pattern.compile("[0-9]*");

        if (!pattern.matcher(phone).matches()) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_PHONE_NO, MsgKeys.IS_INVALID, "Phone number");
        }

        Appointment appointment = new Appointment();
        appointment.setPhone_no(phone_no);
        List<Appointment> appointment_list = db.findByIndex(getSelector(appointment), Appointment.class);
        appointment_list.sort(Comparator.comparing(Appointment::getDatetime));
        return appointment_list.stream().filter(it -> OffsetDateTime.parse(it.getDatetime()).isAfter(OffsetDateTime.now())).collect(Collectors.toList());
    }


    @ApiOperation(value = "Create customer appointment", notes = "Create a new customer appointment", consumes = "application/json", produces = "application/json")
    @ApiImplicitParam(name = "phone_no", value = "phone_no", defaultValue = "61234567", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "branchCodeRequired / datetimeRequired / " +
            "invalidBranchCode / requestedServiceCategoryRequired / " +
            "invalidRequestedServiceCategory / invalidPhoneNo / " +
            "invalidDatetime", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/services/appointments/{phone_no}", method = RequestMethod.POST)
    public Appointment createAppointment(@PathVariable String phone_no,
                                         @RequestBody @ApiParam(value = "Input values for creating appointment(JSON)") AppointmentCreationRequest input,
                                         HttpServletRequest request) throws Exception {
        Database db = getDb(request);

        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        if (!pattern.matcher(phone_no).matches()) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_PHONE_NO, MsgKeys.IS_INVALID, "phone_no");
        }

        if ("".equals(input.getBranch_code()) || input.getBranch_code() == null) {
            throw new Http400Exception(MsgKeys.ERROR_BRANCH_CODE_REQUIRED, MsgKeys.INPUT_REQUIRED, "branch_code");
        }

        if ("".equals(input.getDatetime()) || input.getDatetime() == null) {
            throw new Http400Exception(MsgKeys.ERROR_DATETIME_REQUIRED, MsgKeys.INPUT_REQUIRED, "datetime");
        }
        checkInputDate(input.getDatetime());
        checkService.checkBranch(input.getBranch_code(), MsgKeys.ERROR_INVALID_BRANCH_CODE);


        if ("".equals(input.getRequested_service_category()) || input.getRequested_service_category() == null) {
            throw new Http400Exception(MsgKeys.ERROR_REQUESTED_SERVICE_CATEGORY_REQUIRED, MsgKeys.INPUT_REQUIRED, "requested_service_category");
        }
        if (!Arrays.asList(AppointmentRange).contains(input.getRequested_service_category())) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_REQUESTED_SERVICE_CATEGORY, MsgKeys.IS_INVALID);
        }

        OffsetDateTime date = OffsetDateTime.parse(input.getDatetime());

        Date from = Date.from(date.toInstant());

        //预约时间必须是当前时间后一小时
        if (!from.after(DateUtils.addHours(new Date(), 1))) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_DATETIME, MsgKeys.APPOINTMENT_DATETIME_NOT_MATCH);
        }
        // time should be xx:00, xx:15, xx:30, or xx:45
        if (date.getMinute() != 0 && date.getMinute() != 15 && date.getMinute() != 30 && date.getMinute() != 45) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_DATETIME, MsgKeys.APPOINTMENT_DATETIME_INVALID);
        }

        String dateStr = date.toString();

        //use list size to insert appointment_id
        String appointment_id_max = String.valueOf(getSeqNo(request));

        com.boc.cloud.entity.Appointment appointment_new = new com.boc.cloud.entity.Appointment();
        appointment_new.setType("Appointment");
        appointment_new.setAppointment_id(appointment_id_max);
        appointment_new.setBranch_code(input.getBranch_code());
        appointment_new.setRequested_service_category(input.getRequested_service_category());
        appointment_new.setDatetime(dateStr);
        appointment_new.setPhone_no(phone_no);
        appointment_new.setRequested_service_detail(input.getRequested_service_detail());

        db.save(appointment_new);

        Appointment result = new Appointment();
        result.setAppointment_id(appointment_id_max);
        result.setPhone_no(phone_no);
        result.setBranch_code(input.getBranch_code());
        result.setRequested_service_category(input.getRequested_service_category());
        result.setDatetime(dateStr);
        result.setRequested_service_detail(input.getRequested_service_detail());

        return result;
    }

    @ApiOperation(value = "Cancel customer appointment", notes = "Cancel customer appointment", consumes = "application/json", produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phone_no", value = "phone_no", defaultValue = "61234567", paramType = "path", required = true),
        @ApiImplicitParam(name = "appointment_id", value = "appointment_id", defaultValue = "1", paramType = "path", required = true)})
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "invalidPhoneNo / appointmentNotFound",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/services/appointments/{phone_no}/{appointment_id}", method = RequestMethod.DELETE)
    public AppointmentCancelResponse cancelAppointment(@PathVariable String phone_no,
                                                       @PathVariable String appointment_id,
                                                       HttpServletRequest request) throws Exception {
        Database db = getDb(request);

        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        if (!pattern.matcher(phone_no).matches()) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_PHONE_NO, MsgKeys.IS_INVALID, "Phone number");
        }
        AppointmentCancelResponse result = new AppointmentCancelResponse();
        Appointment appointment = new Appointment();

        appointment.setAppointment_id(appointment_id);
        appointment.setPhone_no(phone_no);

        List<com.boc.cloud.entity.Appointment> master = db.findByIndex(getSelector(appointment), com.boc.cloud.entity.Appointment.class);
        if (master == null || master.size() != 1) {
            throw new Http404Exception(MsgKeys.ERROR_APPOINTMENT_NOT_FOUND, MsgKeys.NOT_EXIST, "appointment_id");
        }
        com.boc.cloud.entity.Appointment appointment_cancel = master.get(0);
        if (!appointment_cancel.getPhone_no().equals(phone_no)) {
            throw new Http404Exception(MsgKeys.ERROR_APPOINTMENT_NOT_FOUND, MsgKeys.NOT_EXIST, "appointment_id");
        }
        db.remove(appointment_cancel);

        result.setStatus("Appointment cancelled successfully");

        return result;
    }
}
