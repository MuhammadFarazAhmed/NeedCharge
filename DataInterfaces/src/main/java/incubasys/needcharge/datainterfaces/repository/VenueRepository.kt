package incubasys.needcharge.datainterfaces.repository

import incubasys.needcharge.datainterfaces.models.Business
import incubasys.needcharge.datainterfaces.models.Direction
import incubasys.needcharge.datainterfaces.models.Geolocation
import incubasys.needcharge.datainterfaces.models.NearbyPlace
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface VenueRepository {
    fun getVenueList(lat: Double, lng: Double): Deferred<Response<List<Business>>>

    fun getVenueDetail(id: Int): Deferred<Response<Business>>

    fun getDirections(origin: String, destination: String, waypoints: String? = null,
                      departure_time: String = "now", trafic_model: String = "best_guess",
                      units: String = "metric"/*, key: String*/):Deferred<Response<Direction>>

    fun getNearbyPlaces(location: String, radius: Int?,
                        lang: String,
                        type: String/*, key: String*/):Deferred<Response<NearbyPlace>>

    fun getAddress(location: String/*, key: String*/):Deferred<Response<Geolocation>>
}