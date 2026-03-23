package es.mobiledev.cpt.ui.screen.testNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.common.EMPTY_STRING
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.webview.component.CPTWebView
import es.mobiledev.commonandroid.util.webview.INDEX_HTML_PATH
import es.mobiledev.commonandroid.util.webview.WebViewContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    onNavigateToWebScreen: (url: String) -> Unit
) {
    val viewModel: TestViewModel = hiltViewModel()
    val uiState by viewModel.getUiState().collectAsStateWithLifecycle()

    BaseScreen(
        isLoading = uiState.isLoading,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.test), color = MaterialTheme.colorScheme.onPrimary)
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(uiState.data.title)
            val contents: List<Pair<String, String>> =
                listOf(
                    "url" to "https://www.google.com",
                    "data" to "<p>Hello World!</p>",
                    "asset" to INDEX_HTML_PATH,
                    "unknown" to EMPTY_STRING
                )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(contents) {
                    Button(
                        onClick = { onNavigateToWebScreen(it.second) }
                    ) {
                        Text(it.first)
                    }
                }
                item {
                    CPTWebView(
                        content =
                            WebViewContent.Html(
                                data =
                                    """
                                    <p>I am an embedded HTML code!</p>
                                    <img src="https://play-lh.googleusercontent.com/y_-anVKl3ID25Je02J1dseqlAm41N8pwI-Gad7aDxPIPss3d7hUYF8c08SNCtwSPW5g" 
                                    width="104" 
                                    height="142" 
                                    alt="Hello Android!"
                                    >
                                    """.trimIndent()
                            )
                    )
                }
            }
        }
    }
}
