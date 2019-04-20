package net.mieczkowski.dal

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
@RunWith(RobolectricTestRunner::class)
open class BaseTest : AutoCloseKoinTest() {

    private val contextModule = module {
        single { ApplicationProvider.getApplicationContext<Context>() }
    }

    @Before
    fun before() {
        startKoin {
            loadKoinModules(contextModule)
        }

        DAL.initCache()
    }
}