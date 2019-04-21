package net.mieczkowski.wordfinder.common.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Josh Mieczkowski on 4/21/2019.
 */
abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {

    var parentAdapter: BaseAdapter<T>? = null

    private var clickListener: ((Context, Int) -> Unit)? = null
    private var longClickListener: ((Context, Int) -> Unit)? = null

    init {
        itemView.setOnClickListener { clickListener?.invoke(itemView.context, adapterPosition) }

        itemView.setOnLongClickListener {
            longClickListener?.invoke(itemView.context, adapterPosition)
            true
        }
    }

    fun bindClickListener(onClick: ((Context, Int) -> Unit)? = null){
        clickListener = onClick
    }

    fun bindLongClickListener(onLongClick: ((Context, Int) -> Unit)? = null){
        longClickListener = onLongClick
    }

    abstract fun bind(item: T)
    fun bindNull(){}

    open fun onDetach() {

    }
}