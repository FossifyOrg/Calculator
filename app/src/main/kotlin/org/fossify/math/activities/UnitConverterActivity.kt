package org.fossify.math.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.performHapticFeedback
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.LOWER_ALPHA_INT
import org.fossify.commons.helpers.MEDIUM_ALPHA_INT
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.math.R
import org.fossify.math.databinding.ActivityUnitConverterBinding
import org.fossify.math.extensions.config
import org.fossify.math.extensions.updateViewColors
import org.fossify.math.helpers.COMMA
import org.fossify.math.helpers.CONVERTER_STATE
import org.fossify.math.helpers.DOT
import org.fossify.math.helpers.converters.Converter
import org.fossify.math.helpers.converters.TemperatureConverter

class UnitConverterActivity : SimpleActivity() {
    companion object {
        const val EXTRA_CONVERTER_ID = "converter_id"
    }

    private val binding by viewBinding(ActivityUnitConverterBinding::inflate)
    private var vibrateOnButtonPress = true
    private lateinit var converter: Converter

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.unitConverterToolbar.inflateMenu(R.menu.converter_menu)
            setupOptionsMenu()
        }

        updateMaterialActivityViews(
            mainCoordinatorLayout = binding.unitConverterCoordinator,
            nestedView = null,
            useTransparentNavigation = false,
            useTopSearchMenu = false
        )
        setupMaterialScrollListener(binding.nestedScrollview, binding.unitConverterToolbar)

        val converter = Converter.ALL.getOrNull(intent.getIntExtra(EXTRA_CONVERTER_ID, 0))

        if (converter == null) {
            finish()
            return
        }
        this.converter = converter

        binding.viewUnitConverter.plusMinusLayout.visibility =
            if (converter.key == TemperatureConverter.key) View.VISIBLE else View.GONE

        binding.viewUnitConverter.btnClear.setVibratingOnClickListener {
            binding.viewUnitConverter.viewConverter.root.deleteCharacter()
        }
        binding.viewUnitConverter.btnClear.setOnLongClickListener {
            binding.viewUnitConverter.viewConverter.root.clear()
            true
        }

        getButtonIds().forEach {
            it.setVibratingOnClickListener { view ->
                binding.viewUnitConverter.viewConverter.root.numpadClicked(view.id)
            }
        }

        binding.viewUnitConverter.viewConverter.root.setConverter(converter)
        binding.unitConverterToolbar.setTitle(converter.nameResId)

        if (savedInstanceState != null) {
            savedInstanceState.getBundle(CONVERTER_STATE)?.also {
                binding.viewUnitConverter.viewConverter.root.restoreFromSavedState(it)
            }
        } else {
            val storedState = config.getLastConverterUnits(converter)
            if (storedState != null) {
                binding.viewUnitConverter.viewConverter.root.updateUnits(
                    newTopUnit = storedState.topUnit,
                    newBottomUnit = storedState.bottomUnit
                )
            }
        }
    }

    private fun setupOptionsMenu() {
        binding.unitConverterToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.swap_units -> binding.viewUnitConverter.viewConverter.root.switch()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun onResume() {
        super.onResume()

        setupToolbar(binding.unitConverterToolbar, NavigationIcon.Arrow)
        binding.viewUnitConverter.viewConverter.root.updateColors()
        binding.viewUnitConverter.converterHolder.let { updateViewColors(it, getProperTextColor()) }

        if (config.preventPhoneFromSleeping) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        setupDecimalSeparator()

        vibrateOnButtonPress = config.vibrateOnButtonPress

        binding.viewUnitConverter.apply {
            arrayOf(btnClear, btnDecimal).forEach {
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    org.fossify.commons.R.drawable.pill_background,
                    theme
                )
                it.background?.alpha = MEDIUM_ALPHA_INT
            }

            arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    org.fossify.commons.R.drawable.pill_background,
                    theme
                )
                it.background?.alpha = LOWER_ALPHA_INT
            }

            if (plusMinusLayout.isVisible) {
                btnPlusMinus.background = ResourcesCompat.getDrawable(
                    resources,
                    org.fossify.commons.R.drawable.pill_background,
                    theme
                )
                btnPlusMinus.background?.alpha = MEDIUM_ALPHA_INT
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (config.preventPhoneFromSleeping) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(
            CONVERTER_STATE,
            binding.viewUnitConverter.viewConverter.root.saveState()
        )
    }

    private fun checkHaptic(view: View) {
        if (vibrateOnButtonPress) {
            view.performHapticFeedback()
        }
    }

    private fun getButtonIds() = binding.viewUnitConverter.run {
        arrayOf(
            btnDecimal, btnPlusMinus, btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9
        )
    }

    private fun View.setVibratingOnClickListener(callback: (view: View) -> Unit) {
        setOnClickListener {
            callback(it)
            checkHaptic(it)
        }
    }

    private fun setupDecimalSeparator() {
        val decimalSeparator: String
        val groupingSeparator: String
        if (config.useCommaAsDecimalMark) {
            decimalSeparator = COMMA
            groupingSeparator = DOT
        } else {
            decimalSeparator = DOT
            groupingSeparator = COMMA
        }
        binding.viewUnitConverter.viewConverter.root.updateSeparators(
            decimalSeparator = decimalSeparator,
            groupingSeparator = groupingSeparator
        )
        binding.viewUnitConverter.btnDecimal.text = decimalSeparator
    }
}
