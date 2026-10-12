package es.mobiledev.commonandroid.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import es.mobiledev.commonandroid.R

@Composable
fun getArticleDetailImageShape() = RoundedCornerShape(size = dimensionResource(R.dimen.article_detail_image__corner_radius))

@Composable
fun getSnackbarShape() = RoundedCornerShape(size = dimensionResource(id = R.dimen.cpt_snackbar__shape))

@Composable
fun getDialogShape() = RoundedCornerShape(size = dimensionResource(id = R.dimen.cpt_dialog__corner_radius))

@Composable
fun getBottomSheetShape() =
    RoundedCornerShape(
        topStart = dimensionResource(id = R.dimen.cpt_bottom_sheet__corner_radius),
        topEnd = dimensionResource(id = R.dimen.cpt_bottom_sheet__corner_radius),
    )
