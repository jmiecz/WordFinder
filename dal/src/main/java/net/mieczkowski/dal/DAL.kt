package net.mieczkowski.dal

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.mieczkowski.dal.network.RetrofitFactory
import net.mieczkowski.dal.network.WfHttpClient
import net.mieczkowski.dal.services.challenges.ChallengesContract
import net.mieczkowski.dal.services.challenges.ChallengesService
import net.mieczkowski.dal.services.challenges.MockedChallengesContract
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.singleBy
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
object DAL : KoinComponent {

    private val networkModule = module {
        single { WfHttpClient.getClient() }
    }

    private val parsingModule = module {
        single {
            jacksonObjectMapper().apply {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
            }
        }
    }

    private val contractModule = module {
        single { RetrofitFactory.createInstance<ChallengesContract>(BuildConfig.BASE_URL) }
    }

    private val mockContractModule = module {
        singleBy<ChallengesContract, MockedChallengesContract>()
    }

    private val serviceModule = module {
        factory<ChallengesService>()
    }


    fun init() {
        initKoin(listOf(networkModule, contractModule))
    }

    fun initCache() {
        initKoin(listOf(mockContractModule))
    }

    private fun initKoin(modules: List<Module>) {
        val modulesToUse = mutableListOf(parsingModule, serviceModule).apply { addAll(0, modules) }
        loadKoinModules(*modulesToUse.toTypedArray())
    }
}