package net.mieczkowski.dal.services.challenges.models

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class ChallengeTest {

    lateinit var testCharGrid: List<List<String>>
    lateinit var testWords: List<WordLocation>
    lateinit var testChallenge: Challenge

    @Before
    fun before(){
        testCharGrid = listOf(
            listOf("A", "B", "C"),
            listOf("D", "F", "G"),
            listOf("H", "I", "J")
        )

        testWords = listOf(
            WordLocation("X", listOf( WordLocation.CharPosition(2, 0))),
            WordLocation("Y", listOf( WordLocation.CharPosition(0, 1))),
            WordLocation("Z", listOf( WordLocation.CharPosition(1, 2)))
        )

        testChallenge = Challenge("", "", testCharGrid, testWords, "")
    }

    @Test
    fun `testing to grid formatted correctly`() {
        val expectedResults = listOf(
            listOf("A", "B", "X"),
            listOf("Y", "F", "G"),
            listOf("H", "Z", "J")
        )
        assertEquals(expectedResults, testChallenge.getGrid())
    }

    @Test
    fun `testing char index returns correct index`(){
        val startingPosition = WordLocation.CharPosition(0, 0)
        assertEquals(0, testChallenge.getCharIndex(startingPosition))

        val secondPosition = WordLocation.CharPosition(1, 0)
        assertEquals(1, testChallenge.getCharIndex(secondPosition))

        val secondRowPosition = WordLocation.CharPosition(0, 1)
        assertEquals(3, testChallenge.getCharIndex(secondRowPosition))


        val lastPosition = WordLocation.CharPosition(testChallenge.getGridWidth() - 1, testChallenge.getGridHeight() - 1)
        assertEquals(8, testChallenge.getCharIndex(lastPosition))


        val middlePosition = WordLocation.CharPosition((testChallenge.getGridWidth() - 1) / 2, (testChallenge.getGridHeight() - 1) / 2)
        assertEquals(4, testChallenge.getCharIndex(middlePosition))
    }

    @Test
    fun `testing correct horizontal indexes`(){
        var startingCharPosition = WordLocation.CharPosition(0, 0)
        var currentIndex = 2
        var expectedSelectedIndexes = listOf(0, 1, 2)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(0, 1)
        currentIndex = 5
        expectedSelectedIndexes = listOf(3, 4, 5)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(2, 2)
        currentIndex = 7
        expectedSelectedIndexes = listOf(7, 8)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))
    }


    @Test
    fun `testing correct vertical indexes`(){
        var startingCharPosition = WordLocation.CharPosition(0, 0)
        var currentIndex = 6
        var expectedSelectedIndexes = listOf(0, 3, 6)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(1, 0)
        currentIndex = 7
        expectedSelectedIndexes = listOf(1, 4, 7)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(2, 2)
        currentIndex = 5
        expectedSelectedIndexes = listOf(5, 8)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))
    }


    @Test
    fun `testing correct diagonal indexes`(){
        var startingCharPosition = WordLocation.CharPosition(1, 1)
        var currentIndex = 0
        var expectedSelectedIndexes = listOf(0, 4)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(1, 1)
        currentIndex = 2
        expectedSelectedIndexes = listOf(2, 4)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(1, 1)
        currentIndex = 6
        expectedSelectedIndexes = listOf(6, 4)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))


        startingCharPosition = WordLocation.CharPosition(1, 1)
        currentIndex = 8
        expectedSelectedIndexes = listOf(8, 4)
        assertEquals(expectedSelectedIndexes, testChallenge.getSelectableIndexes(currentIndex, startingCharPosition))
    }


}