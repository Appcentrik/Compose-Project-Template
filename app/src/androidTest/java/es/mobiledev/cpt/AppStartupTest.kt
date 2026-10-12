package es.mobiledev.cpt

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

/**
 * Smoke test over the real DI graph and the real navigation.
 *
 * It catches what a green build hides. Reaching the home screen means LauncherViewModel was created
 * by Hilt: a ViewModel missing its registration falls back to reflection and dies with "Cannot create
 * an instance of class". A ViewModel reading its state before the subclass initialises it dies with a
 * null StateFlow. Both only ever showed up at runtime.
 *
 * The assertion looks for the article list rather than any copy, because the header is drawn from a
 * drawable and exposes no semantics, and because the list exists whether or not the network answers.
 */
@RunWith(AndroidJUnit4::class)
class AppStartupTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun theAppReachesTheHomeScreenThroughTheRealHiltGraph() {
        composeRule.waitUntil(timeoutMillis = TIMEOUT_MS) {
            composeRule
                .onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.CollectionInfo))
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private companion object {
        const val TIMEOUT_MS = 20_000L
    }
}
