package net.mieczkowski.wordfinder.common.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {

    var iAdapter: AdapterContract<T>? = null

    init {
        if (adapterHandleOnClick()) {
            this.itemView.setOnClickListener { iAdapter?.onItemClick(adapterPosition, itemView.context) }
        } else {
            this.itemView.setOnClickListener(null)
        }
    }

    protected open fun adapterHandleOnClick(): Boolean = true

    fun bindNull() {

    }

    abstract fun bind(item: T)

    open fun onDetach() {

    }
}