package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.mieczkowski.dal.services.challenges.models.Challenge
import net.mieczkowski.wordfinder.R
import net.mieczkowski.wordfinder.common.recyclerview.BaseAdapter
import net.mieczkowski.wordfinder.common.recyclerview.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class GridAdapter(private val challenge: Challenge) : BaseAdapter<String>(challenge.getGrid()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val offset = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            parent.resources.displayMetrics
        ) * challenge.getGridHeight()

        val itemHeight = (parent.measuredHeight - offset.toInt()) / challenge.getGridHeight()

        val view = getView(parent, R.layout.row_grid)

        (view.layoutParams as GridLayoutManager.LayoutParams).let {
            it.height = itemHeight
            view.layoutParams = it
        }

        return GridViewHolder(view)
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

    adapter = GridAdapter(challenge)


}