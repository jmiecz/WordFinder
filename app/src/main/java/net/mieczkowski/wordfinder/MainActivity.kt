package net.mieczkowski.wordfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.mieczkowski.dal.DAL
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            startKoin { androidContext(this@MainActivity) }
            DAL.init()
        }

        setContentView(R.layout.activity_main)
    }
}
