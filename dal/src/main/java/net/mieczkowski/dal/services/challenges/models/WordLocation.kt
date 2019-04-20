package net.mieczkowski.dal.services.challenges.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
data class WordLocation(
    @JsonProperty("word") val word: String,
    @JsonProperty("char_positions") val charPositions: List<CharPosition>
) {
    data class CharPosition(
        @JsonProperty("x") val x: Int,
        @JsonProperty("y") val y: Int
    )
}