package ru.maxultra.stonks.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.FragmentChartBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.util.formatPrice
import ru.maxultra.stonks.util.formatPriceDelta
import ru.maxultra.stonks.util.priceDeltaColor

class ChartFragment : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {

    private val viewModelFactory by lazy { DetailsViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<DetailsViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        val data = prepareDataSet()
        binding.chart.data = LineData(data)

        binding.chart.xAxis.isEnabled = false
        binding.chart.axisLeft.isEnabled = false
        binding.chart.axisRight.isEnabled = false

        binding.chart.legend.isEnabled = false
        binding.chart.description = null
        binding.chart.setNoDataText(getString(R.string.no_chart))

        binding.chart.isDragEnabled = false
        binding.chart.isScaleYEnabled = false
        binding.chart.isDoubleTapToZoomEnabled = false
        binding.chart.isAutoScaleMinMaxEnabled = true

        binding.chart.setViewPortOffsets(4f, 4f, 4f, 4f)

        binding.chart.marker = PriceChartMarker(requireContext(), R.layout.chart_marker).apply {
            chartView = binding.chart
        }

        binding.chart.onChartGestureListener = chartGestureListener

        viewModel.stock.observe(viewLifecycleOwner) {
            if (it != null) {
                val chartData = viewModel.getDisplayChart(it.priceMonth)
                data.values = chartData
                binding.chart.data = LineData(data)
                binding.chart.notifyDataSetChanged()
                binding.chart.invalidate()

                val curr = it.currency!!

                it.currentStockPrice?.let { price ->
                    binding.price.text = formatPrice(curr, price)
                    val diff = if (chartData.isNotEmpty()) price - chartData[0].y else 0.0
                    binding.diff.setTextColor(
                        ContextCompat.getColor(binding.root.context, priceDeltaColor(diff))
                    )
                    binding.diff.text = formatPriceDelta(curr, price, diff)
                }

            }
        }
        return root
    }

    private fun prepareDataSet(): LineDataSet {
        val data = LineDataSet(null, "Values")
        data.setDrawValues(false)
        data.setDrawFilled(true)
        data.lineWidth = 3f
        data.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient)
        data.setDrawCircles(false)
        data.color = ContextCompat.getColor(requireContext(), R.color.black)
        data.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        data.highLightColor = ContextCompat.getColor(requireContext(), R.color.black)
        return data
    }

    private val chartGestureListener = object : OnChartGestureListener {
        override fun onChartGestureStart(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
        ) {
        }

        override fun onChartGestureEnd(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
        ) {
            if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                binding.chart.highlightValues(null)
        }

        override fun onChartLongPressed(me: MotionEvent?) {}
        override fun onChartDoubleTapped(me: MotionEvent?) {}
        override fun onChartSingleTapped(me: MotionEvent?) {}
        override fun onChartFling(
            me1: MotionEvent?,
            me2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ) {
        }

        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}
    }
}
