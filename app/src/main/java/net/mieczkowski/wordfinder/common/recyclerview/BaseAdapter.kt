package net.mieczkowski.wordfinder.common.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Josh Mieczkowski on 4/21/2019.
 */
abstract class BaseAdapter<T>: RecyclerView.Adapter<BaseViewHolder<T>>() {

    private var clickListener: ((Context, Int) -> Unit)? = null
    private var longClickListener: ((Context, Int) -> Unit)? = null

    fun setOnItemClickListener(clickListener: ((Context, Int) -> Unit)? = null) {
        this.clickListener = clickListener
        notifyDataSetChanged()
    }

    fun setOnLongItemClickListener(longClickListener: ((Context, Int) -> Unit)? = null) {
        this.longClickListener = longClickListener
        notifyDataSetChanged()
    }


    abstract fun getData(position: Int): T?

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.parentAdapter = this
        holder.bindClickListener(clickListener)
        holder.bindLongClickListener(longClickListener)

        onDispose(position)

        if (position < 0){
            return
        }

        when (val data = getData(position)) {
            null -> holder.bindNull()
            else -> holder.bind(data)
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    abstract fun onDispose(position: Int)

    protected fun getView(parent: ViewGroup, @LayoutRes layoutID: Int): View =
        LayoutInflater.from(parent.context).inflate(layoutID, parent, false)
}