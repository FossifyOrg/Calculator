package org.fossify.math.extensions

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import org.fossify.math.databases.CalculatorDatabase
import org.fossify.math.helpers.Config
import org.fossify.math.helpers.MyWidgetProvider
import org.fossify.math.interfaces.CalculatorDao
import org.fossify.commons.extensions.*

val Context.config: Config get() = Config.newInstance(applicationContext)

val Context.calculatorDB: CalculatorDao get() = CalculatorDatabase.getInstance(applicationContext).CalculatorDao()

// we are reusing the same layout in the app and widget, but cannot use MyTextView etc in a widget, so color regular view types like this
fun Context.updateViewColors(viewGroup: ViewGroup, textColor: Int) {
    val cnt = viewGroup.childCount
    (0 until cnt).map { viewGroup.getChildAt(it) }
        .forEach {
            when (it) {
                is Button -> it.setTextColor(textColor)
                is TextView -> it.setTextColor(textColor)
                is ViewGroup -> updateViewColors(it, textColor)
            }
        }
}

fun Context.updateWidgets() {
    val widgetIDs = AppWidgetManager.getInstance(applicationContext)?.getAppWidgetIds(ComponentName(applicationContext, MyWidgetProvider::class.java))
        ?: return
    if (widgetIDs.isNotEmpty()) {
        Intent(applicationContext, MyWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs)
            sendBroadcast(this)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Context.launchChangeAppLanguageIntent() {
    try {
        Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            startActivity(this)
        }
    } catch (e: Exception) {
        try {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
                startActivity(this)
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

fun Context.getStrokeColor(): Int {
    return if (isDynamicTheme()) {
        if (isSystemInDarkMode()) {
            resources.getColor(org.fossify.commons.R.color.md_grey_800, theme)
        } else {
            resources.getColor(org.fossify.commons.R.color.md_grey_400, theme)
        }
    } else {
        val lighterColor = getProperBackgroundColor().lightenColor()
        if (lighterColor == Color.WHITE || lighterColor == Color.BLACK) {
            resources.getColor(org.fossify.commons.R.color.divider_grey, theme)
        } else {
            lighterColor
        }
    }
}
