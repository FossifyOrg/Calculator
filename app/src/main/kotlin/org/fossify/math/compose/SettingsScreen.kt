package org.fossify.math.compose

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.fossify.math.R
import org.fossify.commons.compose.alert_dialog.AlertDialogState
import org.fossify.commons.compose.alert_dialog.rememberAlertDialogState
import org.fossify.commons.compose.extensions.BooleanPreviewParameterProvider
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.lists.SimpleColumnScaffold
import org.fossify.commons.compose.settings.SettingsGroup
import org.fossify.commons.compose.settings.SettingsPreferenceComponent
import org.fossify.commons.compose.settings.SettingsSwitchComponent
import org.fossify.commons.compose.settings.SettingsTitleTextComponent
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.divider_grey
import org.fossify.commons.helpers.isTiramisuPlus

@Composable
internal fun SettingsScreen(
    goBack: () -> Unit,
    customizeColors: () -> Unit,
    customizeWidgetColors: () -> Unit,
    preventPhoneFromSleeping: Boolean,
    onPreventPhoneFromSleeping: (Boolean) -> Unit,
    vibrateOnButtonPressFlow: Boolean,
    onVibrateOnButtonPressFlow: (Boolean) -> Unit,
    isOrWasThankYouInstalled: Boolean,
    onThankYou: () -> Unit,
    isUseEnglishEnabled: Boolean,
    isUseEnglishChecked: Boolean,
    onUseEnglishPress: (Boolean) -> Unit,
    onSetupLanguagePress: () -> Unit,
    useCommaAsDecimalMarkFlow: Boolean,
    onUseCommaAsDecimalMarkFlow: (Boolean) -> Unit,
    showCheckmarksOnSwitches: Boolean,
    lockedCustomizeColorText: String,
    displayLanguage: String,
    featureLockedDialogState: AlertDialogState
) {
    SimpleColumnScaffold(title = stringResource(id = org.fossify.commons.R.string.settings), goBack = goBack) {
        SettingsGroup(title = {
            SettingsTitleTextComponent(text = stringResource(id = org.fossify.commons.R.string.color_customization))
        }) {
            SettingsPreferenceComponent(
                label = lockedCustomizeColorText,
                doOnPreferenceClick = {
                    if (isOrWasThankYouInstalled) {
                        customizeColors()
                    } else {
                        featureLockedDialogState.show()
                    }
                },
                preferenceLabelColor = MaterialTheme.colorScheme.onSurface
            )
            SettingsPreferenceComponent(
                label = stringResource(id = org.fossify.commons.R.string.customize_widget_colors),
                doOnPreferenceClick = customizeWidgetColors
            )
        }
        HorizontalDivider(color = divider_grey)
        SettingsGroup(title = {
            SettingsTitleTextComponent(text = stringResource(id = org.fossify.commons.R.string.general_settings))
        }) {
            if (!isOrWasThankYouInstalled) {
                SettingsPreferenceComponent(
                    label = stringResource(id = org.fossify.commons.R.string.purchase_simple_thank_you),
                    doOnPreferenceClick = onThankYou,
                )
            }
            if (isUseEnglishEnabled) {
                SettingsSwitchComponent(
                    label = stringResource(id = org.fossify.commons.R.string.use_english_language),
                    initialValue = isUseEnglishChecked,
                    onChange = onUseEnglishPress,
                    showCheckmark = showCheckmarksOnSwitches
                )
            }
            if (isTiramisuPlus()) {
                SettingsPreferenceComponent(
                    label = stringResource(id = org.fossify.commons.R.string.language),
                    value = displayLanguage,
                    doOnPreferenceClick = onSetupLanguagePress,
                )
            }
            SettingsSwitchComponent(
                label = stringResource(id = R.string.vibrate_on_button_press),
                initialValue = vibrateOnButtonPressFlow,
                onChange = onVibrateOnButtonPressFlow,
                showCheckmark = showCheckmarksOnSwitches
            )
            SettingsSwitchComponent(
                label = stringResource(id = org.fossify.commons.R.string.prevent_phone_from_sleeping),
                initialValue = preventPhoneFromSleeping,
                onChange = onPreventPhoneFromSleeping,
                showCheckmark = showCheckmarksOnSwitches
            )
            SettingsSwitchComponent(
                label = stringResource(id = R.string.use_comma_as_decimal_mark),
                initialValue = useCommaAsDecimalMarkFlow,
                onChange = onUseCommaAsDecimalMarkFlow,
                showCheckmark = showCheckmarksOnSwitches
            )
        }
    }
}

@MyDevices
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showCheckmarksOnSwitches: Boolean
) {
    AppThemeSurface {
        SettingsScreen(
            goBack = {},
            customizeColors = {},
            customizeWidgetColors = {},
            preventPhoneFromSleeping = false,
            onPreventPhoneFromSleeping = {},
            vibrateOnButtonPressFlow = false,
            onVibrateOnButtonPressFlow = {},
            isOrWasThankYouInstalled = true,
            onThankYou = {},
            isUseEnglishEnabled = false,
            isUseEnglishChecked = false,
            onUseEnglishPress = {},
            onSetupLanguagePress = {},
            useCommaAsDecimalMarkFlow = false,
            onUseCommaAsDecimalMarkFlow = {},
            lockedCustomizeColorText = "Customize Colors",
            displayLanguage = "English",
            featureLockedDialogState = rememberAlertDialogState(),
            showCheckmarksOnSwitches = showCheckmarksOnSwitches
        )
    }
}
