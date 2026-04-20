package es.mobiledev.commonandroid.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import es.mobiledev.commonandroid.R

@Composable
fun CPTTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }
            }

            darkTheme ->
                darkColorScheme(
                    scrim = BlueGrey970,
                )

            else ->
                lightColorScheme(
                    scrim = BlueGrey970,
                )
        }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CPTTypography,
        content = content,
    )
}

private val RobotoFontFamily =
    FontFamily(
        Font(
            resId = R.font.roboto_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.roboto_semibold,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resId = R.font.roboto_bold,
            weight = FontWeight.Bold,
        ),
        Font(
            resId = R.font.roboto_extrabold,
            weight = FontWeight.ExtraBold,
        ),
    )

internal val CPTTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 56.sp,
                lineHeight = 1.2.em
            ),
        displayMedium =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 45.sp,
                lineHeight = 1.2.em
            ),
        displaySmall =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 36.sp,
                lineHeight = 1.2.em
            ),
        headlineLarge =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 32.sp,
                lineHeight = 1.2.em
            ),
        headlineMedium =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 28.sp,
                lineHeight = 1.2.em
            ),
        headlineSmall =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 24.sp,
                lineHeight = 1.2.em
            ),
        titleLarge =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 22.sp,
                lineHeight = 1.2.em
            ),
        titleMedium =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 16.sp,
                lineHeight = 1.2.em
            ),
        titleSmall =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 14.sp,
                lineHeight = 1.2.em
            ),
        bodyLarge =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 16.sp,
                lineHeight = 1.2.em,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 14.sp,
                lineHeight = 1.2.em,
            ),
        bodySmall =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 12.sp,
                lineHeight = 1.2.em,
            ),
        labelLarge =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 14.sp,
                lineHeight = 1.2.em,
            ),
        labelMedium =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 12.sp,
                lineHeight = 1.2.em,
            ),
        labelSmall =
            TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 11.sp,
                lineHeight = 1.2.em,
            )
    )
