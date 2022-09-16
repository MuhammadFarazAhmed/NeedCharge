package incubasys.needcharge.repositories.remote.api



import incubasys.needcharge.datainterfaces.models.Direction
import incubasys.needcharge.datainterfaces.models.Geolocation
import incubasys.needcharge.datainterfaces.models.NearbyPlace
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by SamKazmi on 8/24/2017.
 */

interface GoogleApi {

    @GET("directions/json")
    fun getDirections(@Query("origin") origin: String,
                      @Query("destination") destination: String,
                      @Query("waypoints") waypoints: String?,
                      @Query("departure_time") departure_time: String,
                      @Query("traffic_model") traffic_model: String,
                      @Query("units") units: String/*,
                      @Query("key") key: String*/): Deferred<Response<Direction>>


    @GET("place/nearbysearch/json")
    fun getNearByPlaces(@Query("location") location: String,
                        @Query("radius") radius: Int?,
                        @Query("type") type: String,
                        @Query("language") language: String/*,
                        @Query("key") key: String*/):  Deferred<Response<NearbyPlace>>

    @GET("geocode/json")
    fun getAddress(@Query("latlng") latlng: String/*,
                   @Query("key") key: String*/):  Deferred<Response<Geolocation>>
}
