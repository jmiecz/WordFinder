package net.mieczkowski.dal.exts

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Josh Mieczkowski on 11/10/2018.
 */
fun <T> Single<T>.observeOnMain(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnIO(): Single<T> {
    return subscribeOn(Schedulers.io())
}


fun Completable.observeOnMain(): Completable {
    return observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subscribeOnIO(): Completable {
    return subscribeOn(Schedulers.io())
}

