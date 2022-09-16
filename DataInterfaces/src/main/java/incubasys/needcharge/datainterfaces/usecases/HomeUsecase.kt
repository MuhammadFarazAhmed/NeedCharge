package incubasys.needcharge.datainterfaces.usecases

import androidx.databinding.ObservableBoolean
import com.google.android.gms.maps.model.LatLng
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.datainterfaces.models.Direction
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.NearbyPlace
import incubasys.needcharge.datainterfaces.models.User

interface HomeUsecase {

    suspend fun getVenueList(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (list: List<VenueItem>) -> Unit = {},
            onError: (message: Message) -> Unit = {}, lat: Double, lng: Double
    )

    suspend fun getDirections(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (polyLine: List<LatLng>, center: LatLng, stringToShow: String) -> Unit,
            onError: (message: Message) -> Unit = {}, origin: LatLng, destination: LatLng

    )

   /* suspend fun getNearbyPlaces(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (nearbyPlaces: NearbyPlace) -> Unit = {},
            onError: (message: Message) -> Unit = {}, query: String, radius: Int?,
            lang: String,
            type: String
    )*/

    suspend fun getVenueDetail(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: VenueItem) -> Unit = {},
            onError: (message: Message) -> Unit = {}, id: Int
    )


    fun isLoggedIn(): ObservableBoolean
    fun getUser(): User

}
