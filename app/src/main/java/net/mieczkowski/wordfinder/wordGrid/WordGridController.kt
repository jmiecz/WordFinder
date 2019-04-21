package net.mieczkowski.wordfinder.wordGrid

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.controller_word_grid.view.*
import net.mieczkowski.dal.exts.observeOnMain
import net.mieczkowski.dal.services.challenges.ChallengesService
import net.mieczkowski.dal.services.challenges.models.Challenge
import net.mieczkowski.wordfinder.R
import net.mieczkowski.wordfinder.common.controllers.BaseController
import net.mieczkowski.wordfinder.wordGrid.gridAdapter.setChallenge
import org.koin.core.inject

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class WordGridController(args: Bundle? = null) : BaseController(args) {

    private val challengesService by inject<ChallengesService>()

    override fun getLayoutID(): Int = R.layout.controller_word_grid

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        challengesService.getChallenges()
            .observeOnMain()
            .subscribe({
                setAdapter(it)
            }, {
                it.printStackTrace()
                TODO("implement error catching")
            }).also { addSubscriptionForAutoDisposable(it) }
    }

    private fun setAdapter(challenges: List<Challenge>) {
        view?.recyclerGrid?.setChallenge(challenges[0])
    }
}