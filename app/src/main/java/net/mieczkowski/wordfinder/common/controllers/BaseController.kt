package net.mieczkowski.wordfinder.common.controllers

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.mieczkowski.dal.exts.observeOnMain
import org.koin.core.KoinComponent
import java.util.concurrent.TimeUnit

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
abstract class BaseController(args: Bundle? = null) : RestoreViewOnCreateController(args), KoinComponent {
    
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private var snackbar: Snackbar? = null

    protected abstract fun getLayoutID(): Int

    protected abstract fun onViewBound(view: View, savedViewState: Bundle?)

    protected fun addSubscriptionForAutoDisposable(disposable: Disposable?) {
        disposable?.let { compositeDisposable.add(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(getLayoutID(), container, false)
        (view as? ViewGroup)?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        onViewBound(view, savedViewState)

        return view
    }

    override fun onDestroyView(view: View) {
        compositeDisposable.clear()
        if (snackbar?.isShown == true) snackbar?.dismiss()

        super.onDestroyView(view)
    }

    open fun getTitleID(): Int = 0

    fun showSnackbar(
        @StringRes message: Int, durationInMs: Int = 2500,
        viewToUse: View? = view,
        onComplete: (() -> Unit) = {}
    ) {
        viewToUse?.let {
            if (snackbar?.isShown == true) snackbar?.dismiss()

            snackbar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
                .also { it.show() }

            Completable.timer(durationInMs.toLong(), TimeUnit.MILLISECONDS)
                .observeOnMain()
                .subscribe {
                    if (snackbar?.isShown == true) snackbar?.dismiss()
                    onComplete.invoke()
                }.also { addSubscriptionForAutoDisposable(it) }
        }
    }
}