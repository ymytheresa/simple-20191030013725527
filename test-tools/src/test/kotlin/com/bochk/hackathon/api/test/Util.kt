package com.bochk.hackathon.api.test

import com.bochk.hackathon.api.functional.model.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.function.Executable
import java.math.BigDecimal
import java.util.stream.Stream

fun sleep() = Thread.sleep(1000)

fun assertBigDecimalEqualsAfterRounding(expected: BigDecimal, actual: BigDecimal) {
    try {
        assert(expected.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(actual) == 0)
    } catch (ex: AssertionError) {
        // simply for better display of assertion error, so that expected and actual values are shown
        // this is not for real comparison
        assertEquals(expected.setScale(2, BigDecimal.ROUND_HALF_UP), actual)
    }
}

fun ResponseBody.toErrorResponse(): ErrorResponse {
    val errorResponseStr = byteStream().bufferedReader().use { it.readText() }
    return Gson().fromJson<ErrorResponse>(errorResponseStr, ErrorResponse::class.java)
}

// https://github.com/junit-team/junit5/issues/924

fun assertAll(vararg executables: () -> Unit) = assertAll(executables.toList().stream())

fun assertAll(executables: Stream<() -> Unit>) = assertAll(executables.map { Executable(it) })

fun assertAll(heading: String, vararg executables: () -> Unit) = assertAll(heading, executables.toList().stream())

fun assertAll(heading: String, executables: Stream<() -> Unit>) = assertAll(heading, executables.map { Executable(it) })
