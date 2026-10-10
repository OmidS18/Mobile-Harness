package com.jarves.mh.runtime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AntigravityAuthStartupTest {
    @Test
    fun `completion timeout allows five minutes for Google to finish`() {
        assertEquals(300_000L, ANTIGRAVITY_LOGIN_COMPLETION_TIMEOUT_MILLIS)
    }

    @Test
    fun `startup times out at two minutes while waiting for the Google URL`() {
        val start = 100L

        assertFalse(
            isAntigravityLoginStartupTimedOut(
                start,
                start + ANTIGRAVITY_LOGIN_STARTUP_TIMEOUT_MILLIS - 1,
                AntigravityAuthStatus.STARTING,
            ),
        )
        assertTrue(
            isAntigravityLoginStartupTimedOut(
                start,
                start + ANTIGRAVITY_LOGIN_STARTUP_TIMEOUT_MILLIS,
                AntigravityAuthStatus.STARTING,
            ),
        )
    }

    @Test
    fun `completion timeout expires if Google does not finish after code submission`() {
        val submittedAt = 500L

        assertFalse(
            isAntigravityLoginCompletionTimedOut(
                submittedAt,
                submittedAt + ANTIGRAVITY_LOGIN_COMPLETION_TIMEOUT_MILLIS - 1,
                AntigravityAuthStatus.COMPLETING,
            ),
        )
        assertTrue(
            isAntigravityLoginCompletionTimedOut(
                submittedAt,
                submittedAt + ANTIGRAVITY_LOGIN_COMPLETION_TIMEOUT_MILLIS,
                AntigravityAuthStatus.COMPLETING,
            ),
        )
        assertFalse(
            isAntigravityLoginCompletionTimedOut(
                submittedAt,
                submittedAt + ANTIGRAVITY_LOGIN_COMPLETION_TIMEOUT_MILLIS * 10,
                AntigravityAuthStatus.AWAITING_CODE,
            ),
        )
    }

    @Test
    fun `startup timeout does not interrupt browser authorization code entry`() {
        val start = 100L
        val muchLater = start + ANTIGRAVITY_LOGIN_STARTUP_TIMEOUT_MILLIS * 10

        assertFalse(
            isAntigravityLoginStartupTimedOut(
                start,
                muchLater,
                AntigravityAuthStatus.AWAITING_CODE,
            ),
        )
        assertFalse(
            isAntigravityLoginStartupTimedOut(
                start,
                muchLater,
                AntigravityAuthStatus.COMPLETING,
            ),
        )
    }
}
