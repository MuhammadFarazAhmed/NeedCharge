package incubasys.needcharge.datainterfaces.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Message
 */

@Parcelize
data class Message(
        @SerializedName("code") var code: Int = 0, @SerializedName("message")
        var message: String = ""
) : Parcelable



