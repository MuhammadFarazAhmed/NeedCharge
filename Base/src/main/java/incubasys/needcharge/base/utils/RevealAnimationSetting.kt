package incubasys.needcharge.base.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RevealAnimationSetting(
    var centerX: Int = 0,
    var centerY: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) : Parcelable
