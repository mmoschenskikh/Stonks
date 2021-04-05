package ru.maxultra.stonks.ui.details

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import ru.maxultra.stonks.R
import ru.maxultra.stonks.util.formatPrice
import java.text.SimpleDateFormat
import java.util.*

class PriceChartMarker(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    private val priceText = findViewById<TextView>(R.id.priceText)
    private val dateText = findViewById<TextView>(R.id.dateText)
    private val dateFormat = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())
    var currency: Currency = Currency.getInstance("USD")

    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        e?.let {
            priceText.text = formatPrice(currency, it.y.toDouble())
            dateText.text = dateFormat.format(Date(e.x.toLong()))
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offsetValue = 25f
        val xOffset =
            if (posX + offset.x > chartView.width / 2)
                -width - offsetValue
            else
                offsetValue

        val yOffset =
            if (posY + offset.y > chartView.height / 2)
                -height - offsetValue
            else
                offsetValue
        return MPPointF.getInstance(xOffset, yOffset)
    }
}
