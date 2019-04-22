package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import android.view.View
import kotlinx.android.synthetic.main.row_grid.view.*
import net.mieczkowski.wordfinder.common.recyclerview.BaseViewHolder

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class GridViewHolder(itemView: View) : BaseViewHolder<SelectableString>(itemView) {

    private val txtLetter = itemView.txtLetter

    override fun bind(item: SelectableString) {
        txtLetter.text = item.value
        txtLetter.setBackgroundResource(item.colorState.colorID)
    }
}