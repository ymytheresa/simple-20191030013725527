package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.api.InvestmentsApi
import com.bochk.hackathon.api.test.sleep

class InvestmentUtil {

    private val api = com.bochk.hackathon.api.test.functional.FunctionalBaseTest.getApiService(InvestmentsApi::class.java)

    fun getStockQuantity(ibmAppUser: String, stockCode: String): Long {
        sleep()
        val inventory = api.investmentsGet(ibmAppUser).execute().body()!!.filter { it.code == Integer.parseInt(stockCode).toString() }
        assert(inventory.size <= 1)
        if (inventory.isEmpty())
            return 0
        else
            return inventory[0].quantity
    }

}
