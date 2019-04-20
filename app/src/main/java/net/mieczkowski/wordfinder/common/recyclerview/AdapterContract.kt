package net.mieczkowski.wordfinder.common.recyclerview

import android.content.Context
import io.reactivex.disposables.Disposable

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
interface AdapterContract<T> {
    fun removeItem(position: Int)

    fun removeItem(item: T)

    fun addItem(item: T, vararg position: Int)

    fun addItems(items: List<T>, vararg position: Int)

    fun updatedItem(item: T)

    fun updatedItem(position: Int)

    fun replaceItem(item: T)

    fun getItem(position: Int): T

    fun onItemClick(position: Int, context: Context)

    fun itemSize(): Int

    fun addSubscriptionForAutoDisposable(position: Int, disposable: Disposable)

}