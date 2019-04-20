package net.mieczkowski.dal.services.challenges.models

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
)