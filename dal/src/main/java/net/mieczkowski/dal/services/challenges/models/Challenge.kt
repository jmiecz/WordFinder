package net.mieczkowski.dal.services.challenges.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import net.mieczkowski.dal.services.challenges.ChallengesDeserializer

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
@JsonDeserialize(using = ChallengesDeserializer::class)
data class Challenge(
    @JsonProperty("source_language") val sourceLanguage: String,
    @JsonProperty("word") val word: String,
    @JsonProperty("character_grid") val characterGrid: List<List<String>>,
    @JsonProperty("word_locations") val wordLocations: List<WordLocation>,
    @JsonProperty("target_language") val targetLanguage: String
) {
    @JsonIgnore
    fun getGridWidth(): Int = characterGrid.first().size

    @JsonIgnore
    fun getGridHeight(): Int = characterGrid.size

    @JsonIgnore
    fun getGrid(): MutableList<MutableList<String>> {
        val grid: MutableList<MutableList<String>> = characterGrid.map { it.toMutableList() }.toMutableList()

        wordLocations.forEach { wordLocation ->
            wordLocation.charPositions.forEachIndexed { index, charPosition ->
                grid[charPosition.y][charPosition.x] = wordLocation.word[index].toString()
            }
        }

        return grid
    }

    fun getCharPosition(index: Int): WordLocation.CharPosition {
        val x = index % getGridWidth()
        val y = index / getGridWidth()

        return WordLocation.CharPosition(x, y)
    }

    fun getCharIndex(charPosition: WordLocation.CharPosition): Int = getGridWidth() * charPosition.y + charPosition.x

    fun getSelectableIndexes(index: Int, startingCharPosition: WordLocation.CharPosition): List<Int> {
        val currentXY = getCharPosition(index)

        return when {
            startingCharPosition.y == currentXY.y -> handleHorizontal(currentXY, startingCharPosition)
            startingCharPosition.x == currentXY.x -> handleVertical(currentXY, startingCharPosition)
            else -> handleDiagonal(currentXY, startingCharPosition)
        }
    }

    private fun handleHorizontal(
        currentXY: WordLocation.CharPosition,
        startingCharPosition: WordLocation.CharPosition
    ): List<Int> {
        val startingPoint = Math.min(startingCharPosition.x, currentXY.x)
        val endingPoint = Math.max(startingCharPosition.x, currentXY.x)
        val diff = endingPoint - startingPoint

        return (0..diff).map {
            getCharIndex(WordLocation.CharPosition(startingPoint + it, currentXY.y))
        }
    }

    private fun handleVertical(
        currentXY: WordLocation.CharPosition,
        startingCharPosition: WordLocation.CharPosition
    ): List<Int> {
        val startingPoint = Math.min(startingCharPosition.y, currentXY.y)
        val endingPoint = Math.max(startingCharPosition.y, currentXY.y)
        val diff = endingPoint - startingPoint


        return (0..diff).map {
            getCharIndex(WordLocation.CharPosition(currentXY.x, startingPoint + it))
        }
    }

    private fun handleDiagonal(
        currentXY: WordLocation.CharPosition,
        startingCharPosition: WordLocation.CharPosition
    ): List<Int> {
        val diffX = Math.abs(currentXY.x - startingCharPosition.x)
        val diffY = Math.abs(currentXY.y - startingCharPosition.y)

        return if (diffX == diffY) {
            val toReturn = mutableListOf(
                getCharIndex(WordLocation.CharPosition(currentXY.x, currentXY.y)),
                getCharIndex(WordLocation.CharPosition(startingCharPosition.x, startingCharPosition.y))
            )

            var xModifier = 0
            var yModifier = 0

            when {
                currentXY.x < startingCharPosition.x && currentXY.y < startingCharPosition.y -> {
                    xModifier = 1
                    yModifier = 1
                }
                currentXY.x > startingCharPosition.x && currentXY.y < startingCharPosition.y -> {
                    xModifier = -1
                    yModifier = 1
                }
                currentXY.x < startingCharPosition.x && currentXY.y > startingCharPosition.y -> {
                    xModifier = 1
                    yModifier = -1
                }
                currentXY.x > startingCharPosition.x && currentXY.y > startingCharPosition.y -> {
                    xModifier = -1
                    yModifier = -1
                }
            }

            for (index in 1 until diffX) {
                val x = currentXY.x + (index * xModifier)
                val y = currentXY.y + (index * yModifier)

                toReturn.add(getCharIndex(WordLocation.CharPosition(x, y)))
            }

            toReturn
        } else mutableListOf(getCharIndex(WordLocation.CharPosition(startingCharPosition.x, startingCharPosition.y)))
    }
}