package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.api.CustomerOperationsApi
import com.bochk.hackathon.api.functional.model.CustomerProfile
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest.Companion.getApiService
import com.bochk.hackathon.api.test.sleep

class CustomerUtil {

    private val api = getApiService(CustomerOperationsApi::class.java)

    fun getCustomerInfo(ibmAppUser: String): CustomerProfile {
        sleep()
        return api.customerProfileGet(ibmAppUser).execute().body()!!
    }

}
