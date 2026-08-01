package org.fossify.math.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import me.grantland.widget.AutofitHelper
import org.fossify.commons.extensions.appLaunched
import org.fossify.commons.extensions.copyToClipboard
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.hideKeyboard
import org.fossify.commons.extensions.launchMoreAppsFromUsIntent
import org.fossify.commons.extensions.performHapticFeedback
import org.fossify.commons.extensions.toast
import org.fossify.commons.extensions.value
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.APP_ICON_IDS
import org.fossify.commons.helpers.LICENSE_AUTOFITTEXTVIEW
import org.fossify.commons.helpers.LICENSE_EVALEX
import org.fossify.commons.helpers.LOWER_ALPHA_INT
import org.fossify.commons.helpers.MEDIUM_ALPHA_INT
import org.fossify.commons.models.FAQItem
import org.fossify.math.BuildConfig
import org.fossify.math.R
import org.fossify.math.databases.CalculatorDatabase
import org.fossify.math.databinding.ActivityMainBinding
import org.fossify.math.dialogs.HistoryDialog
import org.fossify.math.extensions.config
import org.fossify.math.extensions.updateViewColors
import org.fossify.math.helpers.CALCULATOR_STATE
import org.fossify.math.helpers.Calculator
import org.fossify.math.helpers.CalculatorImpl
import org.fossify.math.helpers.DIVIDE
import org.fossify.math.helpers.HistoryHelper
import org.fossify.math.helpers.MINUS
import org.fossify.math.helpers.MULTIPLY
import org.fossify.math.helpers.PERCENT
import org.fossify.math.helpers.PLUS
import org.fossify.math.helpers.POWER
import org.fossify.math.helpers.ROOT
import org.fossify.math.helpers.getDecimalSeparator

class MainActivity : SimpleActivity(), Calculator {
    private var storedTextColor = 0
    private var vibrateOnButtonPress = true
    private var saveCalculatorState: String = ""
    private lateinit var calc: CalculatorImpl

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appLaunched(BuildConfig.APPLICATION_ID)
        setupOptionsMenu()
        refreshMenuItems()
        setupEdgeToEdge(padBottomSystem = listOf(binding.mainNestedScrollview))
        setupMaterialScrollListener(binding.mainNestedScrollview, binding.mainAppbar!!)

        if (savedInstanceState != null) {
            saveCalculatorState = savedInstanceState.getCharSequence(CALCULATOR_STATE) as String
        }

        calc = CalculatorImpl(
            calculator = this,
            context = applicationContext,
            calculatorState = saveCalculatorState
        )
        binding.btnPlus?.setOnClickOperation(PLUS)
        binding.btnMinus?.setOnClickOperation(MINUS)
        binding.btnMultiply?.setOnClickOperation(MULTIPLY)
        binding.btnDivide?.setOnClickOperation(DIVIDE)
        binding.btnPercent?.setOnClickOperation(PERCENT)
        binding.btnPower?.setOnClickOperation(POWER)
        binding.btnRoot?.setOnClickOperation(ROOT)
        binding.btnMinus?.setOnLongClickListener { calc.turnToNegative() }
        binding.btnClear?.setVibratingOnClickListener { calc.handleClear() }
        binding.btnClear?.setOnLongClickListener {
            calc.handleReset()
            true
        }

        getButtonIds().forEach {
            it?.setVibratingOnClickListener { view ->
                calc.numpadClicked(view.id)
            }
        }

        binding.btnEquals?.setVibratingOnClickListener { calc.handleEquals() }
        binding.formula?.setOnLongClickListener { copyToClipboard(false) }
        binding.result?.setOnLongClickListener { copyToClipboard(true) }
        AutofitHelper.create(binding.result)
        AutofitHelper.create(binding.formula)
        storeStateVariables()
        binding.calculatorHolder?.let { updateViewColors(it, getProperTextColor()) }
        setupDecimalButton()
        checkAppOnSDCard()
    }

    private val keyCodeToButtonId = mapOf(
        KeyEvent.KEYCODE_0 to R.id.btn_0, KeyEvent.KEYCODE_NUMPAD_0 to R.id.btn_0,
        KeyEvent.KEYCODE_1 to R.id.btn_1, KeyEvent.KEYCODE_NUMPAD_1 to R.id.btn_1,
        KeyEvent.KEYCODE_2 to R.id.btn_2, KeyEvent.KEYCODE_NUMPAD_2 to R.id.btn_2,
        KeyEvent.KEYCODE_3 to R.id.btn_3, KeyEvent.KEYCODE_NUMPAD_3 to R.id.btn_3,
        KeyEvent.KEYCODE_4 to R.id.btn_4, KeyEvent.KEYCODE_NUMPAD_4 to R.id.btn_4,
        KeyEvent.KEYCODE_5 to R.id.btn_5, KeyEvent.KEYCODE_NUMPAD_5 to R.id.btn_5,
        KeyEvent.KEYCODE_6 to R.id.btn_6, KeyEvent.KEYCODE_NUMPAD_6 to R.id.btn_6,
        KeyEvent.KEYCODE_7 to R.id.btn_7, KeyEvent.KEYCODE_NUMPAD_7 to R.id.btn_7,
        KeyEvent.KEYCODE_8 to R.id.btn_8, KeyEvent.KEYCODE_NUMPAD_8 to R.id.btn_8,
        KeyEvent.KEYCODE_9 to R.id.btn_9, KeyEvent.KEYCODE_NUMPAD_9 to R.id.btn_9,
        KeyEvent.KEYCODE_PERIOD to R.id.btn_decimal,
        KeyEvent.KEYCODE_COMMA to R.id.btn_decimal,
        KeyEvent.KEYCODE_NUMPAD_DOT to R.id.btn_decimal,
    )

    private val keyCodeToOperation = mapOf(
        KeyEvent.KEYCODE_PLUS to PLUS, KeyEvent.KEYCODE_NUMPAD_ADD to PLUS,
        KeyEvent.KEYCODE_MINUS to MINUS, KeyEvent.KEYCODE_NUMPAD_SUBTRACT to MINUS,
        KeyEvent.KEYCODE_STAR to MULTIPLY, KeyEvent.KEYCODE_NUMPAD_MULTIPLY to MULTIPLY,
        KeyEvent.KEYCODE_SLASH to DIVIDE, KeyEvent.KEYCODE_NUMPAD_DIVIDE to DIVIDE,
    )

    private val equalsKeyCodes = setOf(
        KeyEvent.KEYCODE_EQUALS, KeyEvent.KEYCODE_NUMPAD_EQUALS,
        KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER,
    )

    private val clearKeyCodes = setOf(KeyEvent.KEYCODE_DEL, KeyEvent.KEYCODE_FORWARD_DEL)

    private val resetKeyCodes = setOf(KeyEvent.KEYCODE_ESCAPE, KeyEvent.KEYCODE_CLEAR)

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val buttonId = keyCodeToButtonId[keyCode]
        val operation = keyCodeToOperation[keyCode]
        when {
            buttonId != null -> calc.numpadClicked(buttonId)
            operation != null -> calc.handleOperation(operation)
            keyCode in equalsKeyCodes -> calc.handleEquals()
            keyCode in clearKeyCodes -> calc.handleClear()
            keyCode in resetKeyCodes -> calc.handleReset()
            else -> return super.onKeyDown(keyCode, event)
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.mainAppbar!!)
        setupMaterialScrollListener(binding.mainNestedScrollview, binding.mainAppbar)
        if (storedTextColor != config.textColor) {
            binding.calculatorHolder?.let { updateViewColors(it, getProperTextColor()) }
        }

        if (config.preventPhoneFromSleeping) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        setupDecimalButton()
        vibrateOnButtonPress = config.vibrateOnButtonPress

        binding.apply {
            arrayOf(
                btnPercent, btnPower, btnRoot, btnClear, btnReset, btnDivide, btnMultiply, btnPlus,
                btnMinus, btnEquals, btnDecimal
            ).forEach {
                it?.background = ResourcesCompat.getDrawable(
                    resources, org.fossify.commons.R.drawable.pill_background, theme
                )
                it?.background?.alpha = MEDIUM_ALPHA_INT
            }

            arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it?.background = ResourcesCompat.getDrawable(
                    resources, org.fossify.commons.R.drawable.pill_background, theme
                )
                it?.background?.alpha = LOWER_ALPHA_INT
            }
        }
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
        if (config.preventPhoneFromSleeping) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            CalculatorDatabase.destroyInstance()
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(CALCULATOR_STATE, calc.getCalculatorStateJson().toString())
    }

    private fun setupOptionsMenu() {
        binding.mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history -> showHistory()
                R.id.more_apps_from_us -> launchMoreAppsFromUsIntent()
                R.id.unit_converter -> launchUnitConverter()
                R.id.settings -> launchSettings()
                R.id.about -> launchAbout()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun refreshMenuItems() {
        binding.mainToolbar.menu.apply {
            findItem(R.id.more_apps_from_us).isVisible =
                !resources.getBoolean(org.fossify.commons.R.bool.hide_google_relations)
        }
    }

    private fun storeStateVariables() {
        config.apply {
            storedTextColor = textColor
        }
    }

    private fun checkHaptic(view: View) {
        if (vibrateOnButtonPress) {
            view.performHapticFeedback()
        }
    }

    private fun showHistory() {
        HistoryHelper(this).getHistory {
            if (it.isEmpty()) {
                toast(R.string.history_empty)
            } else {
                HistoryDialog(this, it, calc)
            }
        }
    }

    private fun launchUnitConverter() {
        hideKeyboard()
        startActivity(Intent(applicationContext, UnitConverterPickerActivity::class.java))
    }

    private fun launchSettings() {
        hideKeyboard()
        startActivity(
            Intent(applicationContext, SettingsActivity::class.java).apply {
                putIntegerArrayListExtra(APP_ICON_IDS, getAppIconIDs())
            }
        )
    }

    private fun launchAbout() {
        val licenses = LICENSE_AUTOFITTEXTVIEW or LICENSE_EVALEX

        val faqItems = arrayListOf(
            FAQItem(R.string.faq_1_title, R.string.faq_1_text),
            FAQItem(
                title = org.fossify.commons.R.string.faq_1_title_commons,
                text = org.fossify.commons.R.string.faq_1_text_commons
            ),
            FAQItem(
                title = org.fossify.commons.R.string.faq_4_title_commons,
                text = org.fossify.commons.R.string.faq_4_text_commons
            )
        )

        if (!resources.getBoolean(org.fossify.commons.R.bool.hide_google_relations)) {
            faqItems.add(
                FAQItem(
                    title = org.fossify.commons.R.string.faq_2_title_commons,
                    text = org.fossify.commons.R.string.faq_2_text_commons
                )
            )
            faqItems.add(
                FAQItem(
                    title = org.fossify.commons.R.string.faq_6_title_commons,
                    text = org.fossify.commons.R.string.faq_6_text_commons
                )
            )
        }

        startAboutActivity(
            appNameId = R.string.app_name,
            licenseMask = licenses,
            versionName = BuildConfig.VERSION_NAME,
            faqItems = faqItems,
            showFAQBeforeMail = true
        )
    }

    private fun getButtonIds() = binding.run {
        arrayOf(btnDecimal, btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
    }

    private fun copyToClipboard(copyResult: Boolean): Boolean {
        var value = binding.formula?.value
        if (copyResult) {
            value = binding.result?.value
        }

        return if (value.isNullOrEmpty()) {
            false
        } else {
            copyToClipboard(value)
            true
        }
    }

    override fun showNewResult(value: String, context: Context) {
        binding.result?.text = value
    }

    override fun showNewFormula(value: String, context: Context) {
        binding.formula?.text = value
    }

    private fun setupDecimalButton() {
        binding.btnDecimal?.text = getDecimalSeparator()
    }

    private fun View.setVibratingOnClickListener(callback: (view: View) -> Unit) {
        setOnClickListener {
            callback(it)
            checkHaptic(it)
        }
    }

    private fun View.setOnClickOperation(operation: String) {
        setVibratingOnClickListener {
            calc.handleOperation(operation)
        }
    }
}
