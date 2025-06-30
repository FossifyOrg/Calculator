package org.fossify.math.adapters

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import org.fossify.commons.extensions.applyColorFilter
import org.fossify.commons.extensions.darkenColor
import org.fossify.commons.extensions.getProperBackgroundColor
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.math.R
import org.fossify.math.activities.SimpleActivity
import org.fossify.math.databinding.ItemUnitTypeBinding
import org.fossify.math.extensions.getStrokeColor
import org.fossify.math.helpers.converters.Converter

class UnitTypesAdapter(
    val activity: SimpleActivity,
    val items: List<Converter>,
    val itemClick: (id: Int) -> Unit
) :
    RecyclerView.Adapter<UnitTypesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemUnitTypeBinding.inflate(activity.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item, position)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemUnitTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Converter, id: Int): View {
            itemView.apply {
                binding.unitImage.setImageResource(item.imageResId)
                binding.unitLabel.setText(item.nameResId)

                val rippleBg = ResourcesCompat.getDrawable(
                    resources, R.drawable.unit_type_background, context.theme
                ) as RippleDrawable
                val layerDrawable =
                    rippleBg.findDrawableByLayerId(R.id.background_holder) as LayerDrawable
                layerDrawable.findDrawableByLayerId(R.id.background_stroke)
                    .applyColorFilter(context.getStrokeColor())
                layerDrawable.findDrawableByLayerId(R.id.background_shape)
                    .applyColorFilter(context.getProperBackgroundColor().darkenColor(2))
                binding.unitBackground.background = rippleBg

                binding.unitLabel.setTextColor(activity.getProperTextColor())
                binding.unitImage.applyColorFilter(activity.getProperPrimaryColor())

                setOnClickListener {
                    itemClick(id)
                }
            }

            return itemView
        }
    }
}
