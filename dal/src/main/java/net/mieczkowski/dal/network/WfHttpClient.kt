package net.mieczkowski.dal.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.concurrent.TimeUnit

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
object WfHttpClient : KoinComponent {

    const val cacheSize: Long = 100 * 1024 * 1024 // 100MB
    private const val TIMEOUT_IN_SECONDS = 30L

    fun getClient(): OkHttpClient {
        val context = get<Context>()

        val builder = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, cacheSize))
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()

                val builder: Request.Builder = request.newBuilder()
                builder.url(request.url().toString().replace("%C2%AD", "-"))

                //manage headers

                it.proceed(builder.build())
            }

        return builder.build()
    }

}