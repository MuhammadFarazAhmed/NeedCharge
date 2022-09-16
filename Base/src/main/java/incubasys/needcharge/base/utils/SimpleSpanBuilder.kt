package incubasys.needcharge.base.utils

import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableStringBuilder
import java.util.*

/**
 * Created by Ali on 2/14/2017.
 */

class SimpleSpanBuilder {

    private val spanSections: MutableList<SpanSection>
    private val stringBuilder: StringBuilder = StringBuilder()

    private inner class SpanSection(
        private val text: String,
        private val startIndex: Int,
        private vararg val spans: ParcelableSpan
    ) {
        fun apply(spanStringBuilder: SpannableStringBuilder?) {

            if (spanStringBuilder == null) return
            for (span in spans) {
                spanStringBuilder.setSpan(
                    span,
                    startIndex,
                    startIndex + text.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    init {
        spanSections = ArrayList()
    }

    fun append(text: String, vararg spans: ParcelableSpan): SimpleSpanBuilder {
        if (spans.isNotEmpty()) {
            spanSections.add(SpanSection(text, stringBuilder.length, *spans))
        }
        stringBuilder.append(text)
        return this
    }


    fun build(): SpannableStringBuilder {
        val ssb = SpannableStringBuilder(stringBuilder.toString())
        for (section in spanSections) {
            section.apply(ssb)
        }
        return ssb
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }
}
