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
    fun getGrid(): MutableList<String>{
        val grid: MutableList<MutableList<String>> = characterGrid.map { it.toMutableList() }.toMutableList()

        wordLocations.forEach { wordLocation ->
            wordLocation.charPositions.forEachIndexed { index, charPosition ->
                grid[charPosition.y][charPosition.x] = wordLocation.word[index].toString()
            }
        }

        val toReturn = mutableListOf<String>()
        grid.forEach { toReturn.addAll(it) }

        return toReturn
    }
}