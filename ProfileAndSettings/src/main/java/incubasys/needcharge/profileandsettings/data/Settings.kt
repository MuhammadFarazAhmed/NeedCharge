package incubasys.needcharge.profileandsettings.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(val id: Int, val title: String) : Parcelable