package net.mieczkowski.wordfinder.common.exts

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import org.koin.ext.getFullName

/**
 * Created by Josh Mieczkowski on 4/20/2019.
 */

fun Controller.getTag(): String = this::class.getFullName()

fun Controller.show(
    controller: Controller,
    pushChangeHandler: ControllerChangeHandler = VerticalChangeHandler(),
    popChangeHandler: ControllerChangeHandler = VerticalChangeHandler()
) {
    show(controller.router, pushChangeHandler, popChangeHandler)
}

fun Controller.show(
    router: Router,
    pushChangeHandler: ControllerChangeHandler = VerticalChangeHandler(),
    popChangeHandler: ControllerChangeHandler = VerticalChangeHandler()
) {
    val tag = getTag()

    router.pushController(
        RouterTransaction.with(this)
            .pushChangeHandler(pushChangeHandler)
            .popChangeHandler(popChangeHandler)
            .tag(tag)
    )
}

fun Controller.setRoot(router: Router) {
    router.setRoot(RouterTransaction.with(this))
}

fun Controller.setRoot(
    router: Router,
    pushChangeHandler: ControllerChangeHandler = VerticalChangeHandler(),
    popChangeHandler: ControllerChangeHandler = VerticalChangeHandler()
) {
    val tag = getTag()

    router.setRoot(
        RouterTransaction.with(this)
            .pushChangeHandler(pushChangeHandler)
            .popChangeHandler(popChangeHandler)
            .tag(tag)
    )
}

fun Controller.getRootRouter(): Router = parentController?.getRootRouter() ?: router