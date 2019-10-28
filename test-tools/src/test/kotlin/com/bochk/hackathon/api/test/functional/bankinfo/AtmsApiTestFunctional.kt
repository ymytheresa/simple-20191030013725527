package com.bochk.hackathon.api.test.functional.bankinfo

import com.bochk.hackathon.api.functional.api.BankInformationApi
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AtmsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(BankInformationApi::class.java)

    @Test
    fun `should return a list of 440 ATMs`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.bankInfoAtmsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val atms = response.body()!!

        assertEquals(440, atms.size)
        atms.groupBy { it.districtCode }.forEach { t, u -> println(t + ": " + u.map { it.businessStatus }.toString()) }
    }

    @Test
    fun `should return an ATM in branch`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.bankInfoAtmsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val atms = response.body()!!
        val atm = atms.firstOrNull { it.name == "Central District (Wing On House) Branch" }
        assertNotNull(atm)
        val atm2 = atm!!
        assertEquals("central_western_district", atm2.districtCode)
        assertEquals("Central & Western District", atm2.districtName)
        assertEquals("B/F-2/F, Wing on House, 71 Des Voeux Road Central, Hong Kong", atm2.address)
        assertEquals(22.283852, atm2.addressCoordinates.latitude)
        assertEquals(114.1564086, atm2.addressCoordinates.longitude)
        assertTrue(atm2.services.sorted() == arrayOf(
                "ATM_RMB",
                "CASH_DEPOSIT_DUAL_CURRENCY").sorted())
        assertNotNull(atm2.businessStatus)
    }

    @Test
    fun `should return a standalone ATM`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.bankInfoAtmsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val atms = response.body()!!
        val atm = atms.firstOrNull { it.name == "3/F Bank of China Tower" }
        assertNotNull(atm)
        val atm2 = atm!!
        assertEquals("central_western_district", atm2.districtCode)
        assertEquals("Central & Western District", atm2.districtName)
        assertEquals("3/F Bank of China Tower, 1 Garden Road, HK", atm2.address)
        assertEquals(22.2796675, atm2.addressCoordinates.latitude)
        assertEquals(114.1612875, atm2.addressCoordinates.longitude)
        assertTrue(atm2.services.sorted() == arrayOf(
                "ATM_CENTRE",
                "ATM_RMB",
                "CASH_DEPOSIT_DUAL_CURRENCY",
                "CHEQUE_DEPOSIT").sorted())
        assertNotNull(atm2.businessStatus)
    }

}
