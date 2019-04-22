package net.mieczkowski.dal.services.challenges.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
@Parcelize
data class WordLocation(
    @JsonProperty("word") val word: String,
    @JsonProperty("char_positions") val charPositions: List<CharPosition>
): Parcelable {

    @Parcelize
    data class CharPosition(
        @JsonProperty("x") val x: Int,
        @JsonProperty("y") val y: Int
    ): Parcelable
}