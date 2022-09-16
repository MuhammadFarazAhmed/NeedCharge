package incubasys.needcharge.home.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import incubasys.needcharge.home.R

@BindingAdapter(value = ["venueTypeToString"])
fun venueTypeToString(textView: TextView, type: Int) {
    //  0 => Restuarant 1 => Bar 2 => Gas Station 3 => Hospital 4 => Beauty Salon 5 => Cinema 6 => Motel
    val text = when (type) {
        0 -> "Restuarant"
        1 -> "Bar"
        2 -> "Gas Station"
        3 -> "Hospital"
        4 -> "Beauty Salon"
        5 -> "Cinema"
        6 -> "Motel"
        else -> ""
    }
    textView.text = text
}