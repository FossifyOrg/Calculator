package org.fossify.math.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fossify.commons.activities.CustomizationActivity
import org.fossify.commons.compose.alert_dialog.rememberAlertDialogState
import org.fossify.commons.compose.extensions.enableEdgeToEdgeSimple
import org.fossify.commons.compose.extensions.onEventValue
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.getAppIconIds
import org.fossify.commons.compose.theme.getAppLauncherName
import org.fossify.commons.dialogs.FeatureLockedAlertDialog
import org.fossify.commons.extensions.getCustomizeColorsString
import org.fossify.commons.extensions.isOrWasThankYouInstalled
import org.fossify.commons.extensions.launchPurchaseThankYouIntent
import org.fossify.commons.helpers.APP_ICON_IDS
import org.fossify.commons.helpers.APP_LAUNCHER_NAME
import org.fossify.commons.helpers.IS_CUSTOMIZING_COLORS
import org.fossify.commons.helpers.isTiramisuPlus
import org.fossify.math.compose.SettingsScreen
import org.fossify.math.extensions.config
import org.fossify.math.extensions.launchChangeAppLanguageIntent
import java.util.Locale
import kotlin.system.exitProcess

class SettingsActivity : AppCompatActivity() {

    private val preferences by lazy { config }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val context = LocalContext.current
                val preventPhoneFromSleeping by preferences.preventPhoneFromSleepingFlow
                    .collectAsStateWithLifecycle(preferences.preventPhoneFromSleeping)
                val vibrateOnButtonPressFlow by preferences.vibrateOnButtonPressFlow
                    .collectAsStateWithLifecycle(preferences.vibrateOnButtonPress)
                val wasUseEnglishToggledFlow by preferences.wasUseEnglishToggledFlow
                    .collectAsStateWithLifecycle(preferences.wasUseEnglishToggled)
                val useEnglishFlow by preferences.useEnglishFlow
                    .collectAsStateWithLifecycle(preferences.useEnglish)
                val showCheckmarksOnSwitches by config.showCheckmarksOnSwitchesFlow
                    .collectAsStateWithLifecycle(initialValue = config.showCheckmarksOnSwitches)
                val isUseEnglishEnabled by remember(wasUseEnglishToggledFlow) {
                    derivedStateOf {
                        (wasUseEnglishToggledFlow || Locale.getDefault().language != "en") && !isTiramisuPlus()
                    }
                }
                val isOrWasThankYouInstalled = onEventValue { context.isOrWasThankYouInstalled() }
                val displayLanguage = remember { Locale.getDefault().displayLanguage }
                val featureLockedDialogState = getFeatureLockedDialogState()
                SettingsScreen(
                    displayLanguage = displayLanguage,
                    goBack = ::finish,
                    customizeColors = ::handleCustomizeColorsClick,
                    customizeWidgetColors = ::setupCustomizeWidgetColors,
                    preventPhoneFromSleeping = preventPhoneFromSleeping,
                    onPreventPhoneFromSleeping = preferences::preventPhoneFromSleeping::set,
                    vibrateOnButtonPressFlow = vibrateOnButtonPressFlow,
                    onVibrateOnButtonPressFlow = preferences::vibrateOnButtonPress::set,
                    isOrWasThankYouInstalled = isOrWasThankYouInstalled,
                    onThankYou = ::launchPurchaseThankYouIntent,
                    isUseEnglishEnabled = isUseEnglishEnabled,
                    isUseEnglishChecked = useEnglishFlow,
                    onUseEnglishPress = { isChecked ->
                        preferences.useEnglish = isChecked
                        exitProcess(0)
                    },
                    onSetupLanguagePress = ::launchChangeAppLanguageIntent,
                    showCheckmarksOnSwitches = showCheckmarksOnSwitches,
                    lockedCustomizeColorText = getCustomizeColorsString(),
                    featureLockedDialogState = featureLockedDialogState
                )
            }
        }
    }

    @Composable
    private fun getFeatureLockedDialogState() =
        rememberAlertDialogState().apply {
            DialogMember {
                FeatureLockedAlertDialog(alertDialogState = this, cancelCallback = {})
            }
        }

    private fun handleCustomizeColorsClick() {
        Intent(applicationContext, CustomizationActivity::class.java).apply {
            putExtra(APP_ICON_IDS, getAppIconIds())
            putExtra(APP_LAUNCHER_NAME, getAppLauncherName())
            startActivity(this)
        }
    }

    private fun setupCustomizeWidgetColors() {
        Intent(this, WidgetConfigureActivity::class.java).apply {
            putExtra(IS_CUSTOMIZING_COLORS, true)
            startActivity(this)
        }
    }
}
