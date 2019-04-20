package net.mieczkowski.dal.services.challenges

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import net.mieczkowski.dal.R
import net.mieczkowski.dal.exts.readFromRaw

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
class MockedChallengesContract(context: Context, objectMapper: ObjectMapper) : ChallengesContract {

    private val challenges: String = context.readFromRaw(R.raw.find_challenges)

    override fun getChallenges(): Single<String> = Single.just(challenges)
}