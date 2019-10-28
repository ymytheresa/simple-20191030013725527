package com.bochk.hackathon.api.test.functional.bankinfo

import com.bochk.hackathon.api.functional.api.BankInformationApi
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BranchesApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(BankInformationApi::class.java)

    @Test
    fun `should return a list of 210 bank branches`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.bankInfoBranchesGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val branches = response.body()!!

        assertEquals(210, branches.size)
    }

    @Test
    fun `should return a branch`() {
        val ibmAppUser = "00000000-0000-0000-0000-000000000000"

        val response = api.bankInfoBranchesGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val branches = response.body()!!
        val branch = branches.firstOrNull { it.branchCode == "688" }
        assertNotNull(branch)
        val branch2 = branch!!
        assertEquals("sha_tin_district", branch2.districtCode)
        assertEquals("Shatin District", branch2.districtName)
        assertEquals("Fo Tan Branch", branch2.name)
        assertEquals("No 2, 1/F Shatin Galleria, 18-24 Shan Mei Street, Fo Tan, New Territories", branch2.address)
        assertEquals(22.397241, branch2.addressCoordinates.latitude)
        assertEquals(114.193418, branch2.addressCoordinates.longitude)
        assertEquals("+852 2691 7193", branch2.phoneNo)
        assertEquals("9:00 – 17:00", branch2.openingHours.mon2fri)
        assertEquals("9:00 – 13:00", branch2.openingHours.sat)
        assertEquals("Closed", branch2.openingHours.sunholiday)
        assertTrue(branch2.services.sorted() == arrayOf(
                "ATM_CENTRE",
                "ATM_RMB",
                "RMB",
                "INVESTMENT_SERVICE",
                "SAFE_DEPOSITBOX",
                "SELF_SERVICE",
                "CHEQUE_DEPOSIT",
                "CASH_DEPOSIT_DUAL_CURRENCY",
                "WM_CENTRE",
                "NOTES_PREORDER").sorted())
        assertNotNull(branch2.businessStatus)
    }

}
