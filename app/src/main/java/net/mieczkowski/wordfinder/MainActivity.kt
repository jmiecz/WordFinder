package net.mieczkowski.wordfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import kotlinx.android.synthetic.main.activity_main.*
import net.mieczkowski.dal.DAL
import net.mieczkowski.wordfinder.common.exts.setRoot
import net.mieczkowski.wordfinder.wordGrid.WordGridController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            startKoin { androidContext(this@MainActivity) }
            DAL.init()
        }

        setContentView(R.layout.activity_main)

        router = Conductor.attachRouter(this, rootContainer, savedInstanceState)
        if (!router.hasRootController()) {
            WordGridController().setRoot(router)
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}
