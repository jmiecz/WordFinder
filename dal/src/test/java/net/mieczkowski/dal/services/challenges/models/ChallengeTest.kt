package net.mieczkowski.dal.services.challenges.models

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class ChallengeTest {

    @Test
    fun getGrid() {
        val testCharGrid = listOf(
            listOf("A", "B", "C"),
            listOf("D", "F", "G"),
            listOf("H", "I", "J")
        )

        val testWords = listOf(
            WordLocation("X", listOf( WordLocation.CharPosition(2, 0))),
            WordLocation("Y", listOf( WordLocation.CharPosition(0, 1))),
            WordLocation("Z", listOf( WordLocation.CharPosition(1, 2)))
        )

        val testData = Challenge("", "", testCharGrid, testWords, "")
        assertEquals(listOf("A", "B", "X", "Y", "F", "G", "H", "Z", "J"), testData.getGrid())
    }
}