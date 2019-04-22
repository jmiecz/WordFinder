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
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class WordGridController(args: Bundle? = null) : BaseController(args) {

    companion object {
        private const val CHALLENGES_TAG = "challengesTag"
        private const val INDEX_TAG = "indexTag"
    }

    private val challengesService by inject<ChallengesService>()
    private var challenges = mutableListOf<Challenge>()
    private var currentIndex = 0

    override fun getLayoutID(): Int = R.layout.controller_word_grid

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        if (challenges.isEmpty())
            challengesService.getChallenges()
                .observeOnMain()
                .subscribe({
                    setChallenges(it.toMutableList())
                }, {
                    it.printStackTrace()
                    TODO("implement error catching")
                }).also { addSubscriptionForAutoDisposable(it) }
        else setChallenges(challenges)
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)

        val data = ArrayList<Challenge>().apply { addAll(challenges) }
        outState.putParcelableArrayList(CHALLENGES_TAG, data)
        outState.putInt(INDEX_TAG, currentIndex)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)

        savedViewState.getParcelableArrayList<Challenge>(CHALLENGES_TAG)?.let {
            challenges.clear()
            challenges.addAll(it)
        }

        currentIndex = savedViewState.getInt(INDEX_TAG)

        setChallenges(challenges)
    }

    private fun setChallenges(challenges: MutableList<Challenge>) {
        this.challenges = challenges
        loadChallenge(currentIndex)
    }

    private fun loadChallenge(index: Int = 0) {
        currentIndex = index

        if (index >= challenges.size) {
            loadChallenge(0)
            return
        }

        val challenge = challenges[index]
        applicationContext?.let {
            var title =
                it.getString(R.string.locate_s_in_s, challenge.word, Locale(challenge.targetLanguage).displayLanguage)
            if (challenge.wordLocations.size > 1)
                title += " (${challenge.wordLocations.size})"

            activity?.title = title
        }

        view?.recyclerGrid?.setChallenge(challenge) {
            showSnackbar(
                if (challenge.wordLocations.size > 1) R.string.pural_moving_to_next_word
                else R.string.moving_to_next_word
            ) {
                loadChallenge(index + 1)
            }
        }
    }
}