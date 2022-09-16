package incubasys.needcharge.datainterfaces.models

import com.google.gson.annotations.SerializedName

data class EmailUpdate(
        val password: String,
        @SerializedName("new_email")
        val newEmail: String

)