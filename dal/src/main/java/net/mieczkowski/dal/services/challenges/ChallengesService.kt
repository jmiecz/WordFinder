package net.mieczkowski.dal.services.challenges

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import net.mieczkowski.dal.exts.subscribeOnIO
import net.mieczkowski.dal.services.challenges.models.Challenge

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
class ChallengesService(private val challengesContract: ChallengesContract, private val objectMapper: ObjectMapper) {

    fun getChallenges(): Single<List<Challenge>> =
        challengesContract.getChallenges()
            .map {
                val iterator: MappingIterator<Challenge> = objectMapper.readerFor(Challenge::class.java)
                    .readValues(it)
                iterator.readAll()
            }
            .subscribeOnIO()
}