package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.api.AppointmentsApi
import com.bochk.hackathon.api.functional.model.Appointment
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest.Companion.getApiService
import com.bochk.hackathon.api.test.sleep

class AppointmentUtil {

    private val api = getApiService(AppointmentsApi::class.java)

    fun getAppointment(ibmAppUser: String, phoneNo: String, appointmentId: String): Appointment? {
        sleep()
        val appointments = api.servicesAppointmentsPhoneNoGet(phoneNo, ibmAppUser).execute().body()!!
        return appointments.firstOrNull { it.appointmentId == appointmentId }
    }

}
