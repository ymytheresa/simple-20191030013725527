package com.bochk.hackathon.api.test.functional.appointments

import com.bochk.hackathon.api.functional.api.AppointmentsApi
import com.bochk.hackathon.api.functional.model.AppointmentCreationRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.AppointmentUtil
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit


class AppointmentsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AppointmentsApi::class.java)
    private val util = AppointmentUtil()

    @ParameterizedTest
    @ValueSource(strings = ["", "this is detail"])
    fun `should create appointment`(requestedServiceDetail: String) {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // check response
        assertAll("response",
                { assertNotNull(responseBody.appointmentId) },
                { assertEquals(phoneNo, responseBody.phoneNo) },
                { assertEquals(branchCode, responseBody.branchCode) },
                { assertEquals(datetime, responseBody.datetime) },
                { assertEquals(requestedServiceCategory.value, responseBody.requestedServiceCategory.value) },
                { assertEquals(requestedServiceDetail, responseBody.requestedServiceDetail) }
        )

        // check appointment record
        val appointment = util.getAppointment(ibmAppUser, phoneNo, responseBody.appointmentId)
        assertNotNull(appointment)
        val appointment2 = appointment!!
        assertAll("appointment",
                { assertEquals(responseBody.appointmentId, appointment2.appointmentId) },
                { assertEquals(phoneNo, appointment2.phoneNo) },
                { assertEquals(branchCode, appointment2.branchCode) },
                { assertEquals(datetime, appointment2.datetime) },
                { assertEquals(requestedServiceCategory.value, appointment2.requestedServiceCategory.value) },
                { assertEquals(requestedServiceDetail, appointment2.requestedServiceDetail) }
        )
    }

    @Test
    fun `should list existing apointments in ascending order of timestamp`() {
        val phoneNo = "68686868"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.servicesAppointmentsPhoneNoGet(phoneNo, ibmAppUser).execute()

        assert(response.isSuccessful)
        val appointments = response.body()!!
        assert(appointments.isNotEmpty())
        appointments.forEach({
            assertNotNull(it.appointmentId)
            assertNotNull(it.phoneNo)
            assertNotNull(it.branchCode)
            assertNotNull(it.datetime)
            assertNotNull(it.requestedServiceCategory.value)
        })

        assertEquals(appointments.sortedBy { it.datetime }, appointments)
    }

    @Test
    fun `should cancel an appointment`() {
        val phoneNo = "77777777"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        // make appointment
        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val makeAppointmentResponse = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()
        assert(makeAppointmentResponse.isSuccessful)
        val appointmentId = makeAppointmentResponse.body()!!.appointmentId

        // check appointment record before
        val appointmentBefore = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNotNull(appointmentBefore)

        // cancel appointment
        val cancelAppointmentResponse = api.servicesAppointmentsPhoneNoAppointmentIdDelete(phoneNo, appointmentId, ibmAppUser).execute()
        assert(cancelAppointmentResponse.isSuccessful)

        // check appointment record after
        val appointmentAfter = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNull(appointmentAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234567", "x", "123456789"])
    fun `should reject appointment creation if phone no is invalid`(phoneNo: String) {
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_PHONE_NO, errorResponse.code)
    }

    @Test
    fun `should reject appointment creation if branch code is invalid`() {
        val phoneNo = "68686868"
        val branchCode = "x"
        val datetime = OffsetDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_BRANCH_CODE, errorResponse.code)
    }

    @Test
    fun `should reject appointment creation if datetime is in the past`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().minusMinutes(10).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_DATETIME, errorResponse.code)
    }

    @Test
    fun `should reject appointment creation if datetime is not at least 1hr in the future`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_DATETIME, errorResponse.code)
    }

    @Test
    fun `should reject appointment creation if datetime is not xx00, xx15, xx30, xx45`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(20)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_DATETIME, errorResponse.code)
    }

    @Test
    fun `should reject appointment cancellation if appointment ID and mobile number is not found`() {
        val phoneNo = "33333333"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val cancelPhoneNo = "11111111"
        val cancelAppointmentId = "x"

        // make appointment
        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val makeAppointmentResponse = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()
        assert(makeAppointmentResponse.isSuccessful)
        val appointmentId = makeAppointmentResponse.body()!!.appointmentId

        // check appointment record before
        val appointmentBefore = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNotNull(appointmentBefore)

        // cancel appointment
        val cancelAppointmentResponse = api.servicesAppointmentsPhoneNoAppointmentIdDelete(cancelPhoneNo, cancelAppointmentId, ibmAppUser).execute()
        assert(!cancelAppointmentResponse.isSuccessful)
        assertEquals(404, cancelAppointmentResponse.code())
        val errorResponse = cancelAppointmentResponse.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_APPOINTMENT_NOT_FOUND, errorResponse.code)

        // check appointment record after
        val appointmentAfter = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNotNull(appointmentAfter)
    }

    @Test
    fun `should reject appointment cancellation if only appointment ID is not found`() {
        val phoneNo = "33333333"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val cancelAppointmentId = "x"

        // make appointment
        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val makeAppointmentResponse = api.servicesAppointmentsPhoneNoPost(phoneNo, ibmAppUser, body).execute()
        assert(makeAppointmentResponse.isSuccessful)
        val appointmentId = makeAppointmentResponse.body()!!.appointmentId

        // check appointment record before
        val appointmentBefore = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNotNull(appointmentBefore)

        // cancel appointment
        val cancelAppointmentResponse = api.servicesAppointmentsPhoneNoAppointmentIdDelete(phoneNo, cancelAppointmentId, ibmAppUser).execute()
        assert(!cancelAppointmentResponse.isSuccessful)
        assertEquals(404, cancelAppointmentResponse.code())
        val errorResponse = cancelAppointmentResponse.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_APPOINTMENT_NOT_FOUND, errorResponse.code)

        // check appointment record after
        val appointmentAfter = util.getAppointment(ibmAppUser, phoneNo, appointmentId)
        assertNotNull(appointmentAfter)
    }

}
