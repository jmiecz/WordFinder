package net.mieczkowski.wordfinder.common.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
abstract class BaseAdapter<T>(itemsToAdd: MutableList<T>) : RecyclerView.Adapter<BaseViewHolder<T>>(), AdapterContract<T>,
    KoinComponent {

    var items = itemsToAdd
        set(value) {
            field = value
            handleUpdatingItems()
            notifyDataSetChanged()
        }

    private var clickListener: ((Context, T) -> Unit)? = null

    private val disposables: HashMap<Int, Disposable> = hashMapOf()

    open var addEmptyLastSpace = true

    fun setOnClickListener(clickListener: ((Context, T) -> Unit)? = null) {
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int = items.size + if (addEmptyLastSpace) 1 else 0

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.iAdapter = this

        disposables[position]?.dispose()
        if (position >= items.size) {
            holder.bindNull()
        } else {
            holder.bind(items[position])
        }
    }

    fun getView(parent: ViewGroup, @LayoutRes layoutID: Int): View =
        LayoutInflater.from(parent.context).inflate(layoutID, parent, false)

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    override fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun removeItem(item: T) {
        removeItem(items.indexOf(item))
    }

    override fun addItem(item: T, vararg position: Int) {
        var positionAdded = items.size

        if (position.isNotEmpty()) {
            if (position[0] > items.size) {
                items.add(item)
            } else {
                positionAdded = position[0]
                items.add(positionAdded, item)
            }
        } else {
            items.add(item)
        }

        notifyItemInserted(positionAdded)
    }

    override fun addItems(items: List<T>, vararg position: Int) {
        if (position.isNotEmpty()) {
            this.items.addAll(position[0], items)
            notifyItemRangeInserted(position[0], items.size)

        } else {
            val currentCount = itemCount
            this.items.addAll(items)
            notifyItemRangeInserted(currentCount, items.size)
        }
    }

    override fun updatedItem(item: T) {
        val position = items.indexOf(item)
        notifyItemChanged(position)
    }

    override fun updatedItem(position: Int) {
        notifyItemChanged(position)
    }

    override fun replaceItem(item: T) {
        val index = items.indexOf(item)
        items.removeAt(index)
        items.add(index, item)

        notifyItemChanged(index)
    }

    override fun getItem(position: Int): T = items[position]

    override fun onItemClick(position: Int, context: Context) {
        clickListener?.let {
            it(context, items[position])
        }
    }

    fun moveObjects(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun addSubscriptionForAutoDisposable(position: Int, disposable: Disposable) {
        disposables[position]?.dispose()
        disposables[position] = disposable
    }

    protected open fun handleUpdatingItems() {

    }

    override fun itemSize(): Int = itemCount
}