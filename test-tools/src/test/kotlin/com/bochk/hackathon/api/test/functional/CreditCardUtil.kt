package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.api.CardOperationsApi
import com.bochk.hackathon.api.test.sleep

class CreditCardUtil {

    private val api = com.bochk.hackathon.api.test.functional.FunctionalBaseTest.getApiService(CardOperationsApi::class.java)

    fun getCardCredit(ibmAppUser: String, cardNo: String): Pair<Double, Double> {
        sleep()
        val creditCards = api.creditCardsGet(ibmAppUser).execute().body()!!
        val creditCard = creditCards.firstOrNull { it.cardNo == cardNo }
        return Pair(creditCard?.creditLimit ?: Double.MIN_VALUE, creditCard?.creditUsed ?: Double.MIN_VALUE)
    }

}
