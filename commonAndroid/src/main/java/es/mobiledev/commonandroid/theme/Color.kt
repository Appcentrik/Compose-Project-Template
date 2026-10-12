package es.mobiledev.commonandroid.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// region Light colors
val BlueGrey0Light = Color(0xFFFFFFFF)
val BlueGrey50Light = Color(0xFFF0F2F4)
val BlueGrey100Light = Color(0xFFCED6D9)
val BlueGrey200Light = Color(0xFFBAC5CA)
val BlueGrey300Light = Color(0xFFA6B4BB)
val BlueGrey400Light = Color(0xFF92A4AC)
val BlueGrey500Light = Color(0xFF7F939D)
val BlueGrey600Light = Color(0xFF6B838E)
val BlueGrey700Light = Color(0xFF57727E)
val BlueGrey800Light = Color(0xFF43626F)
val BlueGrey900Light = Color(0xFF305160)
val BlueGrey910Light = Color(0xFF1C4151)
val BlueGrey920Light = Color(0xFF083042)
val BlueGrey930Light = Color(0xFF072837)
val BlueGrey940Light = Color(0xFF05212D)
val BlueGrey950Light = Color(0xFF041922)
val BlueGrey960Light = Color(0xFF031118)
val BlueGrey970Light = Color(0xFF020A0D)
val BlueGrey1000Light = Color(0xFF000000)

val Red0Light = Color(0xFFFF0000)

// endregion

// region Dark colors
val BlueGrey0Dark = Color(0xFF000000)
val BlueGrey50Dark = Color(0xFF020A0D)
val BlueGrey100Dark = Color(0xFF031118)
val BlueGrey200Dark = Color(0xFF041922)
val BlueGrey300Dark = Color(0xFF05212D)
val BlueGrey400Dark = Color(0xFF072837)
val BlueGrey500Dark = Color(0xFF083042)
val BlueGrey600Dark = Color(0xFF1C4151)
val BlueGrey700Dark = Color(0xFF305160)
val BlueGrey800Dark = Color(0xFF43626F)
val BlueGrey900Dark = Color(0xFF57727E)
val BlueGrey910Dark = Color(0xFF6B838E)
val BlueGrey920Dark = Color(0xFF7F939D)
val BlueGrey930Dark = Color(0xFF92A4AC)
val BlueGrey940Dark = Color(0xFFA6B4BB)
val BlueGrey950Dark = Color(0xFFBAC5CA)
val BlueGrey960Dark = Color(0xFFCED6D9)
val BlueGrey970Dark = Color(0xFFF0F2F4)
val BlueGrey1000Dark = Color(0xFFFFFFFF)

val Red0Dark = Color(0xFFFF0000)

// endregion

// region Themed colors
val BlueGrey0
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey0Light,
            darkColor = BlueGrey0Dark,
        )
val BlueGrey50
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey50Light,
            darkColor = BlueGrey50Dark,
        )
val BlueGrey100
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey100Light,
            darkColor = BlueGrey100Dark,
        )
val BlueGrey200
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey200Light,
            darkColor = BlueGrey200Dark,
        )
val BlueGrey300
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey300Light,
            darkColor = BlueGrey300Dark,
        )
val BlueGrey400
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey400Light,
            darkColor = BlueGrey400Dark,
        )
val BlueGrey500
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey500Light,
            darkColor = BlueGrey500Dark,
        )
val BlueGrey600
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey600Light,
            darkColor = BlueGrey600Dark,
        )
val BlueGrey700
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey700Light,
            darkColor = BlueGrey700Dark,
        )
val BlueGrey800
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey800Light,
            darkColor = BlueGrey800Dark,
        )
val BlueGrey900
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey900Light,
            darkColor = BlueGrey900Dark,
        )
val BlueGrey910
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey910Light,
            darkColor = BlueGrey910Dark,
        )
val BlueGrey920
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey920Light,
            darkColor = BlueGrey920Dark,
        )
val BlueGrey930
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey930Light,
            darkColor = BlueGrey930Dark,
        )
val BlueGrey940
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey940Light,
            darkColor = BlueGrey940Dark,
        )
val BlueGrey950
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey950Light,
            darkColor = BlueGrey950Dark,
        )
val BlueGrey960
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey960Light,
            darkColor = BlueGrey960Dark,
        )
val BlueGrey970
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey970Light,
            darkColor = BlueGrey970Dark,
        )
val BlueGrey1000
    @Composable get() =
        getColorForTheme(
            lightColor = BlueGrey1000Light,
            darkColor = BlueGrey1000Dark,
        )

val Red0
    @Composable get() =
        getColorForTheme(
            lightColor = Red0Light,
            darkColor = Red0Dark,
        )

// endregion

// region Component colors
val ArticleCardLight = Color(0xFFE5EDF1)

// endregion

// region Home
val ArticleCard
    @Composable get() =
        getColorForTheme(
            lightColor = ArticleCardLight,
            darkColor = BlueGrey100Dark,
        )

// endregion

@Composable
private fun getColorForTheme(
    lightColor: Color,
    darkColor: Color
): Color =
    if (isSystemInDarkTheme()) {
        darkColor
    } else {
        lightColor
    }
