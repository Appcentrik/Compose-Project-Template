package es.mobiledev.feature.webscreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey0
import es.mobiledev.commonandroid.theme.BlueGrey100
import es.mobiledev.commonandroid.theme.BlueGrey800

@Composable
fun WebViewErrorDialog(
    onReload: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
    ) {
        Column(
            Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(32.dp))
                .background(color = BlueGrey100)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_close_filled),
                    contentDescription = null
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null
                )
                Text(
                    text = "Oops, it looks like there was a problem",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onReload,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = BlueGrey800,
                            contentColor = BlueGrey0
                        )
                ) {
                    Text("Reload")
                }
            }
        }
    }
}
