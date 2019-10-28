package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest.Companion.getApiService
import com.bochk.hackathon.api.test.functional.accounts.ThirdPartyNameApiTestFunctional
import com.bochk.hackathon.api.test.sleep

class AccountUtil {

    private val api = getApiService(AccountOperationsApi::class.java)

    fun getAccountBalance(ibmAppUser: String, accountNo: String, currency: Currency): Double {
        sleep()
        val accounts = api.accountsGet(ibmAppUser).execute().body() ?: return Double.MIN_VALUE
        val account = accounts.firstOrNull { it.accountNo == accountNo } ?: return Double.MIN_VALUE
        val balances = account.balances
        return balances.firstOrNull { it.currency == currency }?.balance ?: 0.toDouble()
    }

    fun getLastTransaction(ibmAppUser: String, accountNo: String): AccountTransaction? {
        sleep()
        val transactions = api.accountsAccountNoTransactionsGet(accountNo, ibmAppUser).execute().body()!!
        return transactions.sortedByDescending { it.datetime }.first()
    }

    fun getTransactionCount(ibmAppUser: String, accountNo: String): Int {
        sleep()
        return api.accountsAccountNoTransactionsGet(accountNo, ibmAppUser).execute().body()?.size ?: Int.MIN_VALUE
    }

    fun getThirdPartyName(ibmAppUser: String, accountInfoType: ThirdPartyNameApiTestFunctional.AccountInfoType, accountInfo: String): String {
        sleep()
        return api.accountsThirdPartyNameGet(accountInfoType.name, accountInfo, ibmAppUser).execute().body()!!.name
    }

}
