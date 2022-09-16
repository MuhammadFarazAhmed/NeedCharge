package incubasys.needcharge.datainterfaces

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusinessTimings(val day: String, val timings: String) :Parcelable


