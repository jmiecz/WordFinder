package net.mieczkowski.wordfinder

import android.os.Bundle
import android.view.View
import net.mieczkowski.wordfinder.common.controllers.BaseController

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */
class MainController(args: Bundle? = null) : BaseController(args) {

    override fun getLayoutID(): Int = R.layout.controller_main

    override fun onViewBound(view: View, savedViewState: Bundle?) {

    }
}