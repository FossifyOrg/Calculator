package org.fossify.calculator.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fossify.calculator.databinding.HistoryViewBinding
import org.fossify.calculator.activities.SimpleActivity
import org.fossify.calculator.helpers.CalculatorImpl
import org.fossify.calculator.models.History
import org.fossify.commons.extensions.copyToClipboard
import org.fossify.commons.extensions.getProperTextColor

class HistoryAdapter(val activity: SimpleActivity, val items: List<History>, val calc: CalculatorImpl, val itemClick: () -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var textColor = activity.getProperTextColor()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(HistoryViewBinding.inflate(activity.layoutInflater, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: HistoryViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: History): View {
            itemView.apply {
                binding.itemFormula.text = item.formula
                binding.itemResult.text = item.result
                binding.itemFormula.setTextColor(textColor)
                binding.itemResult.setTextColor(textColor)

                setOnClickListener {
                    calc.addNumberToFormula(item.result)
                    itemClick()
                }

                setOnLongClickListener {
                    activity.baseContext.copyToClipboard(item.result)
                    true
                }
            }

            return itemView
        }
    }
}
