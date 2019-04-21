package net.mieczkowski.dal.services.challenges

import net.mieczkowski.dal.BaseTest
import net.mieczkowski.dal.services.challenges.models.WordLocation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.inject

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
class ChallengesServiceTest : BaseTest() {

    private val challengesService by inject<ChallengesService>()

    @Test
    fun `testing challenges parsed correctly`() {
        val obsTest = challengesService.getChallenges().test()
        obsTest.await()

        obsTest.assertValue {
            assertEquals(true, it.isNotEmpty())

            val toTest = it[0]
            assertEquals("en", toTest.sourceLanguage)
            assertEquals("man", toTest.word)
            assertEquals("es", toTest.targetLanguage)

            assertEquals(8, toTest.getGridWidth())
            assertEquals(8, toTest.getGridHeight())

            toTest.characterGrid[0].let { char ->
                assertEquals("i", char[0])
                assertEquals("q", char[1])
                assertEquals("í", char[2])
                assertEquals("l", char[3])
                assertEquals("n", char[4])
                assertEquals("n", char[5])
                assertEquals("m", char[6])
                assertEquals("ó", char[7])
            }

            assertEquals(1, toTest.wordLocations.size)
            toTest.wordLocations[0].let { wordLocation ->
                assertEquals("hombre", wordLocation.word)
                assertEquals(wordLocation.word.length, wordLocation.charPositions.size)

                assertEquals(WordLocation.CharPosition(6, 1), wordLocation.charPositions[0])
                assertEquals(
                    WordLocation.CharPosition(6, 6),
                    wordLocation.charPositions[wordLocation.charPositions.size - 1]
                )
            }

            true
        }
    }
}