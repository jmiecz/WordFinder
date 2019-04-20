package net.mieczkowski.dal.network

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
object RetrofitFactory : KoinComponent {

    val client by inject<OkHttpClient>()
    val objectMapper by inject<ObjectMapper>()

    inline fun <reified T> createInstance(baseUrl: String): T =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build().create(T::class.java)

}