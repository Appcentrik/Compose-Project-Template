package es.mobiledev.cpt.ui.screen.testNavigation

data class TestUiState(
    val lastAction: String = "",
    val actionInvocations: Int = 0,
    val snackBarsShown: Int = 0,
)
