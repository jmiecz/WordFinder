package net.mieczkowski.dal.services.challenges

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import net.mieczkowski.dal.services.challenges.models.Challenge
import net.mieczkowski.dal.services.challenges.models.WordLocation

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
class ChallengesDeserializer : StdDeserializer<Challenge>(Challenge::class.java) {

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Challenge {
        val mapper = jp.codec as ObjectMapper
        val node: JsonNode = mapper.readTree(jp)

        val sourceLanguage = node.get("source_language").textValue()
        val word = node.get("word").textValue()
        val targetLanguage = node.get("target_language").textValue()

        val characterGrid = mutableListOf<MutableList<String>>()
        node.get("character_grid").forEach { parent ->
            val child = mutableListOf<String>()
            parent.forEach {
                child.add(it.textValue())
            }

            characterGrid.add(child)
        }


        val wordLocationsNode = node.get("word_locations")
        val keys = wordLocationsNode.fieldNames().asSequence().toList()

        val wordLocations = wordLocationsNode.mapIndexed { index, parent ->
            val xYs = keys[index].split(",")
            val charPositions = mutableListOf<WordLocation.CharPosition>()

            for (position in 0..xYs.size - 2 step 2) {
                val x = xYs[position].toInt()
                val y = xYs[position + 1].toInt()
                charPositions.add(WordLocation.CharPosition(x, y))
            }

            WordLocation(parent.textValue(), charPositions)
        }

        return Challenge(sourceLanguage, word, characterGrid, wordLocations, targetLanguage)
    }
}