package com.voidx.github.util.action

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher

object CommonActions {

    fun waitFor(timeInMillis: Long): ViewAction =
        object : ViewAction {
            override fun getDescription(): String {
                return "waiting for $timeInMillis"
            }

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(timeInMillis)
            }
        }

    fun slowSwipeUp(): ViewAction =
        actionWithAssertions(
            GeneralSwipeAction(
                Swipe.SLOW,
                GeneralLocation.VISIBLE_CENTER,
                GeneralLocation.TOP_CENTER,
                Press.FINGER
            )
        )
}