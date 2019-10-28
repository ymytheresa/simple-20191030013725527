package com.bochk.hackathon.api.test.oauth.appointments

import com.bochk.hackathon.api.oauth.api.AppointmentsApi
import com.bochk.hackathon.api.oauth.model.AppointmentCreationRequest
import com.bochk.hackathon.api.test.ERROR_CLIENT_CRED_GRANT_REQUIRED
import com.bochk.hackathon.api.test.oauth.OAuthBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class AppointmentsApiTestOAuth : OAuthBaseTest() {

    private val api = getApiService(AppointmentsApi::class.java, GrantType.CLIENT_CREDENTIALS)
    private val apiAuthorizationCodeGrant = getApiService(AppointmentsApi::class.java, GrantType.AUTHORIZATION_CODE)
    private val apiNoAuth = getApiService(AppointmentsApi::class.java)

    @Test
    fun `should create appointment if client_credentials grant is used`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "detail"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = api.servicesAppointmentsPhoneNoPost(phoneNo, body).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should list existing apointments if client_credentials grant is used`() {
        val phoneNo = "68686868"
        val response = api.servicesAppointmentsPhoneNoGet(phoneNo).execute()

        assert(response.isSuccessful)
        response.body()!!
    }

    @Test
    fun `should cancel an appointment if client_credentials grant is used`() {
        val phoneNo = "77777777"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "this is detail"

        // make appointment
        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val makeAppointmentResponse = api.servicesAppointmentsPhoneNoPost(phoneNo, body).execute()
        assert(makeAppointmentResponse.isSuccessful)
        val appointmentId = makeAppointmentResponse.body()!!.appointmentId

        // cancel appointment
        val cancelAppointmentResponse = api.servicesAppointmentsPhoneNoAppointmentIdDelete(phoneNo, appointmentId).execute()
        assert(cancelAppointmentResponse.isSuccessful)
    }

    @Test
    fun `should reject appointment creation if authorization_code grant is used`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "detail"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = apiAuthorizationCodeGrant.servicesAppointmentsPhoneNoPost(phoneNo, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_CLIENT_CRED_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject appointment creation if no OAuth is used`() {
        val phoneNo = "68686868"
        val branchCode = "916"
        val datetime = OffsetDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES).withMinute(15)
        val requestedServiceCategory = AppointmentCreationRequest.RequestedServiceCategoryEnum.CROSS_BORDER_SERVICES
        val requestedServiceDetail = "detail"

        val body = AppointmentCreationRequest()
                .branchCode(branchCode)
                .datetime(datetime)
                .requestedServiceCategory(requestedServiceCategory)
                .requestedServiceDetail(requestedServiceDetail)
        val response = apiNoAuth.servicesAppointmentsPhoneNoPost(phoneNo, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

    @Test
    fun `should reject appointment list if authorization_code grant is used`() {
        val phoneNo = "68686868"

        val response = apiAuthorizationCodeGrant.servicesAppointmentsPhoneNoGet(phoneNo).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_CLIENT_CRED_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject appointment list if no OAuth is used`() {
        val phoneNo = "68686868"

        val response = apiNoAuth.servicesAppointmentsPhoneNoGet(phoneNo).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

    @Test
    fun `should reject appointment cancellation if authorization_code grant is used`() {
        val phoneNo = "77777777"
        val appointmentId = "123"

        val response = apiAuthorizationCodeGrant.servicesAppointmentsPhoneNoAppointmentIdDelete(phoneNo, appointmentId).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_CLIENT_CRED_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject appointment cancellation if no OAuth is used`() {
        val phoneNo = "77777777"
        val appointmentId = "123"

        val response = apiNoAuth.servicesAppointmentsPhoneNoAppointmentIdDelete(phoneNo, appointmentId).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

}
