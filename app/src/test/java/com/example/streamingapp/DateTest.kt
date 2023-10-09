package com.example.streamingapp

import app.utils.date.DateFromNow
import app.utils.date.LocalDateTimeExtendedFunctions.parseWithTodayAndYesterdayNaming
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTest {

    @Test
    fun `test parseWithTodayAndYesterdayNaming with today date returns parsed day name`() {
        var a = "2023-10-09T01:32:24.157Z"
        var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        var date123 = LocalDateTime.parse(a, dtf)

        var date = LocalDateTime.now()
        var expectedTime = date.format(DateTimeFormatter.ofPattern("HH:mm"))
        var expectedDate = DateFromNow.TODAY
        var actualDate = date.parseWithTodayAndYesterdayNaming()?.dateFromNow
        var actualTime = date.parseWithTodayAndYesterdayNaming()?.time

        Assert.assertEquals(expectedDate, actualDate)
        Assert.assertEquals(expectedTime, actualTime)
    }

    @Test
    fun `test parseWithTodayAndYesterdayNaming with tomorrow date returns parsed day name`() {
        var date = LocalDateTime.now()
        date = date.plusDays(1)
        var expectedTime = date.format(DateTimeFormatter.ofPattern("HH:mm"))
        var expectedDate = DateFromNow.TOMORROW
        var actualDate = date.parseWithTodayAndYesterdayNaming()?.dateFromNow
        var actualTime = date.parseWithTodayAndYesterdayNaming()?.time

        Assert.assertEquals(expectedDate, actualDate)
        Assert.assertEquals(expectedTime, actualTime)
    }

    @Test
    fun `test parseWithTodayAndYesterdayNaming with yesterday date  returns parsed day name`() {
        var date = LocalDateTime.now()
        date = date.minusDays(1)
        var expectedTime = date.format(DateTimeFormatter.ofPattern("HH:mm"))
        var expectedDate = DateFromNow.YESTERDAY
        var actualDate = date.parseWithTodayAndYesterdayNaming()?.dateFromNow
        var actualTime = date.parseWithTodayAndYesterdayNaming()?.time

        Assert.assertEquals(expectedDate, actualDate)
        Assert.assertEquals(expectedTime, actualTime)
    }

    @Test
    fun `test parseWithTodayAndYesterdayNaming with dayafter tommorow date  returns parsed day name`() {
        var date = LocalDateTime.now()
        date = date.plusDays(2)
        var expectedTime = date.format(DateTimeFormatter.ofPattern("HH:mm"))
        var expectedDate = DateFromNow.NONE
        var actualDate = date.parseWithTodayAndYesterdayNaming()?.dateFromNow
        var actualTime = date.parseWithTodayAndYesterdayNaming()?.time

        Assert.assertEquals(expectedDate, actualDate)
        Assert.assertEquals(expectedTime, actualTime)
    }
}