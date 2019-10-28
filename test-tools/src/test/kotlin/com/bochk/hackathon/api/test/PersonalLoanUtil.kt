package com.bochk.hackathon.api.test

import kotlin.math.roundToLong

class PersonalLoanUtil {

    fun calculateInstallmentAmount(amount: Long, yearlyRate: Double, noOfTerms: Long): Long {
        val monthlyRate = yearlyRate / 12
        return (amount * (monthlyRate + (monthlyRate / (Math.pow(1 + monthlyRate, noOfTerms.toDouble()) - 1)))).roundToLong()
    }

}
