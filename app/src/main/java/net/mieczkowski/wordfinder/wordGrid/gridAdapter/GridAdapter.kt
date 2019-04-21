package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.dragselectrecyclerview.DragSelectTouchListener
import com.afollestad.dragselectrecyclerview.Mode
import net.mieczkowski.dal.services.challenges.models.Challenge
import net.mieczkowski.wordfinder.R
import net.mieczkowski.wordfinder.common.recyclerview.BaseAdapter
import net.mieczkowski.wordfinder.common.recyclerview.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
data class SelectableString(val value: String, var isSelected: Boolean = false)

class GridAdapter(val challenge: Challenge) : BaseAdapter<SelectableString>() {

    private val grid = challenge.getGrid()
    private val selectableStrings = mutableListOf<SelectableString>()
    private var offset: Int = -1

    override fun getData(position: Int): SelectableString? {
        return if (position >= selectableStrings.size) {
            val charPosition = challenge.getCharPosition(position)
            SelectableString(grid[charPosition.y][charPosition.x], false).also { selectableStrings.add(it) }
        } else selectableStrings[position]
    }

    override fun onDispose(position: Int) {

    }

    override fun getItemCount(): Int {
        var itemCount = 0
        grid.forEach { itemCount += it.size }

        return itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SelectableString> {
        if (offset == -1) {
            offset = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                parent.resources.displayMetrics
            ).toInt() * (challenge.getGridHeight() + 1)
        }

        val itemHeight = (parent.measuredHeight - offset) / challenge.getGridHeight()

        val view = getView(parent, R.layout.row_grid)

        (view.layoutParams as GridLayoutManager.LayoutParams).let {
            it.height = itemHeight
            view.layoutParams = it
        }

        return GridViewHolder(view)
    }

    fun deselectItems() {
        selectableStrings.filter { it.isSelected }.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
}

fun RecyclerView.setChallenge(challenge: Challenge) {
    layoutManager = GridLayoutManager(context, challenge.getGridWidth())

    val drawableDividerLine =
        ContextCompat.getDrawable(context, R.drawable.divider_line) ?: throw Exception("Missing image")

    addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL).apply {
            setDrawable(drawableDividerLine)
        }
    )

    addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(drawableDividerLine)
        }
    )

    adapter = GridAdapter(challenge).also {
        val gridDragSelectReceiver = GridDragSelectReceiver(it)
        val dragListener = DragSelectTouchListener.create(context, gridDragSelectReceiver) {
            disableAutoScroll()
            mode = Mode.PATH
        }

        addOnItemTouchListener(dragListener)

        it.setOnLongItemClickListener { context, position ->
            gridDragSelectReceiver.setStartingIndex(position)
            dragListener.setIsActive(true, position)
        }
    }


}