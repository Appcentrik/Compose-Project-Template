package es.mobiledev.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed interface that represents the different screens of the application.
 */
@Serializable
sealed interface AppScreens {
    val module: NavigationModule
    val hasTopBar: Boolean
    val hasBottomBar: Boolean

    //region LAUNCHER
    @Serializable
    data object Launcher : AppScreens {
        override val module: NavigationModule = NavigationModule.LAUNCHER
        override val hasTopBar: Boolean = false
        override val hasBottomBar: Boolean = false
    }
    //endregion

    //region HOME
    @Serializable
    data object Home : AppScreens {
        override val module: NavigationModule = NavigationModule.HOME
        override val hasTopBar: Boolean = true
        override val hasBottomBar: Boolean = true
    }
    //endregion

    //region TEST
    @Serializable
    data object Test : AppScreens {
        override val module: NavigationModule = NavigationModule.TEST
        override val hasTopBar: Boolean = true
        override val hasBottomBar: Boolean = true
    }
    //endregion

    //region ARTICLE DETAIL
    @Serializable
    data class ArticleDetail(
        val id: Long
    ) : AppScreens {
        override val module: NavigationModule = NavigationModule.ARTICLE_DETAIL
        override val hasTopBar: Boolean = true
        override val hasBottomBar: Boolean = true
    }
    //endregion

    //region WEB SCREEN
    @Serializable
    data class WebScreen(
        val url: String
    ) : AppScreens {
        override val module: NavigationModule = NavigationModule.TEST
        override val hasTopBar: Boolean = true
        override val hasBottomBar: Boolean = true
    }
    //endregion
}
