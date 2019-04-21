package net.mieczkowski.wordfinder.wordGrid.gridAdapter

import com.afollestad.dragselectrecyclerview.DragSelectReceiver
import net.mieczkowski.dal.services.challenges.models.WordLocation

/**
 * Created by Josh Mieczkowski on 4/21/2019.
 */
class GridDragSelectReceiver(private val gridAdapter: GridAdapter) : DragSelectReceiver {

    private var startingIndex = -1
    private var startingXY = WordLocation.CharPosition(-1, -1)

    fun setStartingIndex(index: Int) {
        startingIndex = index
        startingXY = gridAdapter.challenge.getCharPosition(index)
    }

    override fun getItemCount(): Int = gridAdapter.itemCount

    override fun isIndexSelectable(index: Int): Boolean = true

    override fun isSelected(index: Int): Boolean = gridAdapter.getData(index)?.isSelected == true

    override fun setSelected(index: Int, selected: Boolean) {
        if (index == startingIndex){
            if(selected){
                gridAdapter.getData(index)?.isSelected = selected
                gridAdapter.notifyItemChanged(index)
            }

            return
        }

        gridAdapter.deselectItems()
        gridAdapter.challenge.getSelectableIndexs(index, startingXY).forEach {
            gridAdapter.getData(it)?.isSelected = true
            gridAdapter.notifyItemChanged(it)
        }
    }
}