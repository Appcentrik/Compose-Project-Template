package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.ui.component.popup.SNACKBAR_DISMISS_DELAY_MS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Covers the regressions found running the flows on a device: the action button used to hide the
 * snackbar without running anything, and a snackbar without action kept the button and never auto
 * dismissed.
 */
@RunWith(AndroidJUnit4::class)
class UiErrorSnackbarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tappingTheActionRunsIt() {
        var actions = 0

        composeRule.setContent {
            UiErrorSnackbar(
                uiError = snackBarWithAction,
                onAction = { actions++ },
            )
        }

        composeRule.onNodeWithText(RETRY).performClick()

        assertEquals("Tapping the action must run it", 1, actions)
    }

    @Test
    fun tappingTheActionRunsItOnlyOnce() {
        var actions = 0

        composeRule.setContent {
            UiErrorSnackbar(
                uiError = snackBarWithAction,
                onAction = { actions++ },
            )
        }

        composeRule.onNodeWithText(RETRY).performClick()
        composeRule.waitForIdle()

        assertEquals(1, actions)
    }

    @Test
    fun withoutAnActionNoButtonIsRendered() {
        setContentSettled {
            UiErrorSnackbar(uiError = snackBarWithoutAction)
        }

        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()
        assertEquals(
            "A snackbar with no action must not render the action button",
            0,
            composeRule.onAllNodesWithText(RETRY).fetchSemanticsNodes().size,
        )
    }

    @Test
    fun withoutAnActionItDismissesItselfAndReportsTheDismissal() {
        var dismissals = 0

        setContentSettled {
            UiErrorSnackbar(
                uiError = snackBarWithoutAction,
                onDismiss = { dismissals++ },
            )
        }
        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()
        assertEquals("It must stay visible while counting down", 0, dismissals)

        composeRule.mainClock.advanceTimeBy(SNACKBAR_DISMISS_DELAY_MS + 1000L)
        composeRule.waitForIdle()

        assertEquals("Hiding on its own must report the dismissal", 1, dismissals)
    }

    @Test
    fun aSnackbarShownAfterADismissalIsVisible() {
        val current = mutableStateOf<UiError.SnackBar?>(snackBarWithoutAction)
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            current.value?.let { uiError ->
                UiErrorSnackbar(
                    uiError = uiError,
                    onDismiss = { current.value = null },
                )
            }
        }
        settleEnterAnimation()
        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()

        composeRule.mainClock.advanceTimeBy(SNACKBAR_DISMISS_DELAY_MS + 1000L)
        composeRule.waitForIdle()
        assertTrue(
            "The auto dismissed snackbar must be gone",
            composeRule.onAllNodesWithText(NETWORK_TITLE).fetchSemanticsNodes().isEmpty(),
        )

        composeRule.runOnIdle { current.value = snackBarWithoutAction }
        settleEnterAnimation()

        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()
    }

    @Test
    fun replacingOneSnackbarWithAnotherShowsTheNewOne() {
        val current = mutableStateOf<UiError.SnackBar>(snackBarWithoutAction)
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            UiErrorSnackbar(uiError = current.value)
        }
        settleEnterAnimation()
        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()

        composeRule.runOnIdle {
            current.value =
                UiError.SnackBar(
                    title = R.string.error_server_title,
                    message = R.string.error_server_message,
                    actionLabel = R.string.error_action_retry,
                )
        }
        settleEnterAnimation()

        composeRule.onNodeWithText(SERVER_TITLE).assertIsDisplayed()
        assertTrue(
            "The replaced snackbar must be gone",
            composeRule.onAllNodesWithText(NETWORK_TITLE).fetchSemanticsNodes().isEmpty(),
        )
    }

    /**
     * Sets the content with the clock taken over, then lets the enter animation finish.
     *
     * Auto advancing would run the five second countdown to completion before the test could look at
     * anything, while a frozen clock would leave the snackbar still translated out of view, so the
     * clock is frozen and then advanced just past the enter transition.
     */
    private fun setContentSettled(content: @Composable () -> Unit) {
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent(content)
        settleEnterAnimation()
    }

    private fun settleEnterAnimation() {
        composeRule.mainClock.advanceTimeBy(ENTER_ANIMATION_MS)
        composeRule.waitForIdle()
    }

    private companion object {
        const val NETWORK_TITLE = "No internet connection"
        const val SERVER_TITLE = "Server error"
        const val RETRY = "Retry"
        const val ENTER_ANIMATION_MS = 1_000L

        val snackBarWithAction =
            UiError.SnackBar(
                title = R.string.error_network_title,
                message = R.string.error_network_message,
                actionLabel = R.string.error_action_retry,
            )

        val snackBarWithoutAction =
            UiError.SnackBar(
                title = R.string.error_network_title,
                message = R.string.error_network_message,
                actionLabel = null,
            )
    }
}
