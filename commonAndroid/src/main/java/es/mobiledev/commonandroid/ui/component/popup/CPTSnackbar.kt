package es.mobiledev.commonandroid.ui.component.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.common.DEFAULT_COMPONENT_WEIGHT
import es.mobiledev.common.ONE_FLOAT
import es.mobiledev.common.SNACKBAR_WITHOUT_ACTION_TIMEOUT_MS
import es.mobiledev.common.SNACKBAR_WITH_ACTION_TIMEOUT_MS
import es.mobiledev.common.ZERO_FLOAT
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.BlueGrey800
import es.mobiledev.commonandroid.theme.BlueGrey930
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.theme.CptOutline
import es.mobiledev.commonandroid.theme.CptSurfaceElevated
import es.mobiledev.commonandroid.theme.bodySmallRobotoRegular
import es.mobiledev.commonandroid.theme.getSnackbarShape
import es.mobiledev.commonandroid.theme.labelLargeRobotoSemiBold
import es.mobiledev.commonandroid.theme.titleMediumRobotoBold

/**
 * CPT snackbar.
 *
 * Displays a transient message over the screen content. It hides itself after
 * [SNACKBAR_WITH_ACTION_TIMEOUT_MS] when [actionLabel] and [onAction] are both provided, or after
 * [SNACKBAR_WITHOUT_ACTION_TIMEOUT_MS] otherwise. Only the variant without an action renders the
 * countdown progress bar.
 *
 * @param title headline text. Hidden when blank.
 * @param message supporting text. Hidden when blank.
 * @param modifier the [Modifier] to be applied to the snackbar.
 * @param actionLabel label of the optional action. Ignored when [onAction] is null.
 * @param onAction invoked when the action is tapped. The snackbar hides afterwards.
 * @param onDismiss invoked once the snackbar has finished hiding.
 * @param trailingContent optional content rendered after the texts.
 * @param leadingContent optional content rendered before the texts, typically an icon.
 */
@Composable
fun CPTSnackbar(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    trailingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    val hasAction = actionLabel != null && onAction != null
    val state =
        rememberSnackbarState(
            title = title,
            message = message,
            actionLabel = actionLabel,
            durationMillis =
                if (hasAction) {
                    SNACKBAR_WITH_ACTION_TIMEOUT_MS
                } else {
                    SNACKBAR_WITHOUT_ACTION_TIMEOUT_MS
                },
            onDismiss = onDismiss,
        )

    AnimatedVisibility(
        visibleState = state.visibility,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        CPTSnackbarSurface(
            modifier = modifier,
            action =
                CPTSnackbarAction(
                    label = actionLabel.orEmpty(),
                    onClick = {
                        onAction?.invoke()
                        state.visibility.targetState = false
                    },
                ).takeIf { hasAction },
        ) {
            CPTSnackbarContent(
                title = title,
                message = message,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                progress = state.progress.value.takeIf { !hasAction },
            )
        }
    }
}

@Composable
private fun CPTSnackbarSurface(
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape = getSnackbarShape()

    Snackbar(
        action = action,
        actionOnNewLine = true,
        shape = shape,
        containerColor = CptSurfaceElevated,
        content = content,
        modifier =
            modifier
                .padding(all = dimensionResource(id = R.dimen.cpt_snackbar__padding))
                .shadow(
                    elevation = dimensionResource(id = R.dimen.cpt_snackbar__elevation),
                    shape = shape,
                    clip = false,
                ).border(
                    width = dimensionResource(id = R.dimen.cpt_snackbar__border_width),
                    color = CptOutline,
                    shape = shape,
                ),
    )
}

@Composable
private fun CPTSnackbarContent(
    title: String,
    message: String,
    leadingContent: @Composable (() -> Unit)?,
    trailingContent: @Composable (() -> Unit)?,
    progress: Float?,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.cpt_snackbar__content_spacing),
            ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.cpt_snackbar__row_spacing),
                ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            leadingContent?.invoke()
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement =
                    Arrangement.spacedBy(
                        space = dimensionResource(id = R.dimen.cpt_snackbar__text_spacing),
                    ),
                modifier = Modifier.weight(weight = DEFAULT_COMPONENT_WEIGHT),
            ) {
                if (title.isNotBlank()) {
                    Text(
                        text = title,
                        style = titleMediumRobotoBold(color = BlueGrey930),
                    )
                }
                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        style = bodySmallRobotoRegular(color = BlueGrey800),
                    )
                }
            }
            trailingContent?.invoke()
        }
        progress?.let { value ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(fraction = value)
                        .padding(
                            bottom = dimensionResource(id = R.dimen.cpt_snackbar__progress_padding_bottom),
                        ).height(
                            height = dimensionResource(id = R.dimen.cpt_snackbar__progress_height),
                        ).clip(shape = CircleShape)
                        .background(color = BlueGrey800),
            )
        }
    }
}

@Composable
private fun CPTSnackbarAction(
    label: String,
    onClick: () -> Unit,
): @Composable () -> Unit =
    {
        TextButton(onClick = onClick) {
            Text(
                text = label,
                style = labelLargeRobotoSemiBold(color = BlueGrey930),
            )
        }
    }

@Composable
private fun rememberSnackbarState(
    title: String,
    message: String,
    actionLabel: String?,
    durationMillis: Int,
    onDismiss: () -> Unit,
): SnackbarState {
    val visibility =
        remember {
            MutableTransitionState(initialState = false).apply {
                targetState = true
            }
        }
    val progress = remember { Animatable(initialValue = ONE_FLOAT) }

    LaunchedEffect(title, message, actionLabel) {
        visibility.targetState = true
        progress.snapTo(targetValue = ONE_FLOAT)
        progress.animateTo(
            targetValue = ZERO_FLOAT,
            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing),
        )
        visibility.targetState = false
    }

    LaunchedEffect(visibility.currentState) {
        if (!visibility.currentState && !visibility.targetState) {
            onDismiss()
        }
    }

    return SnackbarState(visibility = visibility, progress = progress)
}

private class SnackbarState(
    val visibility: MutableTransitionState<Boolean>,
    val progress: Animatable<Float, AnimationVector1D>,
)

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.dp16),
                ),
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = BlueGrey50)
                    .padding(all = dimensionResource(id = R.dimen.dp16)),
        ) {
            CPTSnackbar(
                title = "Auto-dismiss",
                message = "Watch the progress bar at the bottom",
            )
            CPTSnackbar(
                title = "Persistent",
                message = "This one has an action, so no progress bar",
                actionLabel = "Confirm",
                onAction = {},
            )
        }
    }
}
