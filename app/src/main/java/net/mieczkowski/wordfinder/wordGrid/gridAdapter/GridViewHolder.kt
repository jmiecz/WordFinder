package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import android.view.View
import kotlinx.android.synthetic.main.row_grid.view.*
import net.mieczkowski.wordfinder.common.recyclerview.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class GridViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    private val txtLetter = itemView.txtLetter

    override fun bind(item: String) {
        txtLetter.text = item
    }
}