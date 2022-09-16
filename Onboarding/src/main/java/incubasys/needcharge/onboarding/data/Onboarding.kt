package incubasys.needcharge.onboarding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Onboarding(
    var id: Int = 0,
    var bgId: Int,
    var resId: Int,
    var title: String,
    var description: String
) : Parcelable
