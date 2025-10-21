package org.fossify.math.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.commons.views.AutoGridLayoutManager
import org.fossify.math.R
import org.fossify.math.adapters.UnitTypesAdapter
import org.fossify.math.databinding.ActivityUnitConverterPickerBinding
import org.fossify.math.extensions.config
import org.fossify.math.helpers.converters.Converter

class UnitConverterPickerActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityUnitConverterPickerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateEdgeToEdge(
            topAppBar = binding.unitConverterPickerToolbar,
            scrollingView = binding.unitTypesGrid,
        )
        setupMaterialScrollListener(binding.unitTypesGrid, binding.unitConverterPickerToolbar)

        binding.unitTypesGrid.layoutManager =
            AutoGridLayoutManager(this, resources.getDimensionPixelSize(R.dimen.unit_type_size))
        binding.unitTypesGrid.adapter = UnitTypesAdapter(this, Converter.ALL) {
            Intent(this, UnitConverterActivity::class.java).apply {
                putExtra(UnitConverterActivity.EXTRA_CONVERTER_ID, it)
                startActivity(this)
            }
        }

        binding.unitConverterPickerToolbar.setTitle(R.string.unit_converter)
    }

    override fun onResume() {
        super.onResume()

        setupTopAppBar(binding.unitConverterPickerToolbar, NavigationIcon.Arrow)

        if (config.preventPhoneFromSleeping) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onPause() {
        super.onPause()
        if (config.preventPhoneFromSleeping) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
