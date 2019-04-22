package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.dragselectrecyclerview.DragSelectTouchListener
import com.afollestad.dragselectrecyclerview.Mode
import io.reactivex.Completable
import net.mieczkowski.dal.exts.observeOnMain
import net.mieczkowski.dal.services.challenges.models.Challenge
import net.mieczkowski.dal.services.challenges.models.WordLocation
import net.mieczkowski.wordfinder.R
import net.mieczkowski.wordfinder.common.recyclerview.BaseAdapter
import net.mieczkowski.wordfinder.common.recyclerview.BaseViewHolder
import net.mieczkowski.wordfinder.common.recyclerview.NoScrollGridLayoutManager
import java.util.concurrent.TimeUnit

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
data class SelectableString(val value: String, var colorState: ColorState = ColorState.Unselected) {
    enum class ColorState(@ColorRes val colorID: Int) {
        Unselected(R.color.grid_unselected),
        Selected(R.color.grid_selected),
        Incorrect(R.color.grid_incorrect),
        Correct(R.color.grid_correct)
    }
}

class GridAdapter(challenge: Challenge) : BaseAdapter<SelectableString>() {

    var challenge: Challenge = challenge
        set(value) {
            grid.clear()
            grid.addAll(value.getGrid())

            selectableStrings.clear()

            field = value
        }

    private val grid = challenge.getGrid()
    private val selectableStrings = mutableListOf<SelectableString>()
    private var foundWords = mutableListOf<WordLocation>()

    override fun getData(position: Int): SelectableString? {
        return if (position >= selectableStrings.size) {
            val charPosition = challenge.getCharPosition(position)
            SelectableString(grid[charPosition.y][charPosition.x]).also { selectableStrings.add(it) }
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
        val itemHeight = parent.measuredHeight / challenge.getGridHeight()

        val view = getView(parent, R.layout.row_grid)

        (view.layoutParams as GridLayoutManager.LayoutParams).let {
            it.height = itemHeight
            view.layoutParams = it
        }

        return GridViewHolder(view)
    }

    fun deselectItems() {
        selectableStrings.forEachIndexed { index, selectableString ->
            if (
                selectableString.colorState == SelectableString.ColorState.Selected ||
                selectableString.colorState == SelectableString.ColorState.Incorrect
            ) {
                selectableString.colorState = SelectableString.ColorState.Unselected
                notifyItemChanged(index)
            }
        }
    }

    private fun checkIfLetterInFound(index: Int): Boolean {
        foundWords.forEach {
            val charPosition = challenge.getCharPosition(index)
            if (it.charPositions.contains(charPosition))
                return true
        }

        return false
    }

    fun validateSelection(): Boolean {
        val selectedWord = selectableStrings.mapIndexed { index, selectableString ->
            if (selectableString.colorState == SelectableString.ColorState.Selected) selectableString.value
            else ""
        }.filter { !it.isBlank() }.joinToString(separator = "")

        val wordLocation = challenge.wordLocations.firstOrNull { it.word.toLowerCase() == selectedWord.toLowerCase() }
        val colorState =
            if (wordLocation != null) {
                if (!foundWords.contains(wordLocation)) foundWords.add(wordLocation)

                SelectableString.ColorState.Correct
            } else SelectableString.ColorState.Incorrect

        selectableStrings.forEachIndexed { index, selectableString ->
            if (selectableString.colorState == SelectableString.ColorState.Selected) {
                selectableString.colorState =
                    if (checkIfLetterInFound(index)) SelectableString.ColorState.Correct
                    else colorState
                notifyItemChanged(index)
            }
        }

        return foundWords.size == challenge.wordLocations.size
    }
}

fun RecyclerView.setChallenge(challenge: Challenge, onWordFound: () -> Unit) {
    layoutManager = NoScrollGridLayoutManager(context, challenge.getGridWidth())

    if (adapter == null) {
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
    }

    adapter = GridAdapter(challenge).also {
        val gridDragSelectReceiver = GridDragSelectReceiver(it)
        val dragListener = DragSelectTouchListener.create(context, gridDragSelectReceiver) {
            disableAutoScroll()
            mode = Mode.PATH
        }

        var onLongClicked = false
        addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return dragListener.onInterceptTouchEvent(rv, e)
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (onLongClicked && e.action == ACTION_UP) {
                    onLongClicked = false
                    if (it.validateSelection()) {
                        onWordFound.invoke()
                    } else {
                        Completable.timer(1200, TimeUnit.MILLISECONDS)
                            .observeOnMain()
                            .subscribe { it.deselectItems() }
                    }
                }

                dragListener.onTouchEvent(rv, e)
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                dragListener.onRequestDisallowInterceptTouchEvent(disallowIntercept)
            }
        })

        it.setOnLongItemClickListener { context, position ->
            onLongClicked = true
            gridDragSelectReceiver.setStartingIndex(position)
            dragListener.setIsActive(true, position)
        }
    }

}