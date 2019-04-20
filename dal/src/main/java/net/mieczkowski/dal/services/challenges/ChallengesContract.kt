package net.mieczkowski.dal.services.challenges

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
interface ChallengesContract {

    @Headers("Content-Type: text/plain")
    @GET("duolingo­data/s3/js2/find_challenges.txt")
    fun getChallenges(): Single<String>
}