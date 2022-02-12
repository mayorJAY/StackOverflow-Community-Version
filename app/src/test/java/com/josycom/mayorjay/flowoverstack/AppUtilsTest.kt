package com.josycom.mayorjay.flowoverstack

import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.junit.Assert
import org.junit.Test

class AppUtilsTest {

    @Test
    fun `test toNormalDate pass_in_valid_time return a non_null value`() {
        val result = AppUtils.toNormalDate(System.currentTimeMillis())
        Assert.assertNotNull(result)
    }

    @Test
    fun `test toNormalDate pass_in_valid_time return a valid format`() {
        val result = AppUtils.toNormalDate(System.currentTimeMillis())
        val regex = "^[a-zA-Z]{3}\\s[0-9]{2}\\s[0-9]{4,}$".toRegex()
        val match = regex.matches(result!!)
        Assert.assertTrue(match)
    }

    @Test
    fun `test getFormattedTags pass_in_empty_list return empty String`() {
        val result = AppUtils.getFormattedTags(listOf())
        Assert.assertEquals("", result)
    }

    @Test
    fun `test getFormattedTags pass_in_valid_list return list joined as a single String`() {
        val list = listOf("Java", "Kotlin", "Android", "JetPack")
        val result = AppUtils.getFormattedTags(list)
        Assert.assertEquals("Java, Kotlin, Android, JetPack", result)
    }
}