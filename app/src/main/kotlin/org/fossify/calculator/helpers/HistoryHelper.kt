package org.fossify.calculator.helpers

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.fossify.calculator.extensions.calculatorDB
import org.fossify.calculator.models.History
import org.fossify.commons.helpers.ensureBackgroundThread

class HistoryHelper(val context: Context) {
    fun getHistory(callback: (calculations: ArrayList<History>) -> Unit) {
        ensureBackgroundThread {
            val notes = context.calculatorDB.getHistory() as ArrayList<History>

            Handler(Looper.getMainLooper()).post {
                callback(notes)
            }
        }
    }

    fun insertOrUpdateHistoryEntry(entry: History) {
        ensureBackgroundThread {
            context.calculatorDB.insertOrUpdate(entry)
        }
    }
}
