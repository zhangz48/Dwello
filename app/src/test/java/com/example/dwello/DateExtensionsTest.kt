package com.example.dwello

import com.example.dwello.ui.daysAgo
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Date

class DateExtensionsTest {
    @Test
    fun testDaysAgo() {
        val currentDate = Date()

        // Test with the same date (should return 0)
        assertEquals(0, currentDate.daysAgo())

        // Test with a date 5 days ago
        val fiveDaysAgo = Date(currentDate.time - 5 * 24 * 60 * 60 * 1000)
        assertEquals(5, fiveDaysAgo.daysAgo())

        // Test with a date 30 days ago
        val thirtyDaysAgo = Date(currentDate.time - 30 * 24 * 60 * 60 * 1000)
        assertEquals(30, thirtyDaysAgo.daysAgo())
    }
}