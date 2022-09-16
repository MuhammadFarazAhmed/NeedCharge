package incubasys.needcharge.repositories

import incubasys.needcharge.datainterfaces.models.Business
import incubasys.needcharge.datainterfaces.models.Direction
import incubasys.needcharge.datainterfaces.models.Geolocation
import incubasys.needcharge.datainterfaces.models.NearbyPlace
import incubasys.needcharge.datainterfaces.repository.VenueRepository
import incubasys.needcharge.repositories.remote.api.GoogleApi
import incubasys.needcharge.repositories.remote.api.VenueApi
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class VenueRepositoryImp @Inject constructor(
        private val venueApi: VenueApi, private val googleApi: GoogleApi
) : VenueRepository {
    override fun getDirections(origin: String, destination: String, waypoints: String?, departure_time: String, trafic_model: String, units: String): Deferred<Response<Direction>> = googleApi.getDirections(origin, destination, waypoints, departure_time, trafic_model, units)

    override fun getNearbyPlaces(location: String, radius: Int?, lang: String, type: String): Deferred<Response<NearbyPlace>> = googleApi.getNearByPlaces(location, radius, lang, type)

    override fun getAddress(location: String): Deferred<Response<Geolocation>> = googleApi.getAddress(location)

    override fun getVenueList(lat: Double, lng: Double): Deferred<Response<List<Business>>> = venueApi.getVenuesList(lat, lng)

    override fun getVenueDetail(id: Int): Deferred<Response<Business>> = venueApi.getVenuesDetail(id)
}