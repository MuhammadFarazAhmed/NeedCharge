package incubasys.needcharge.repositories.remote.api

import incubasys.needcharge.datainterfaces.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface VenueApi {

    @GET("businesses/")
    fun getVenuesList(@Query("lat") lat: Double, @Query("lng") lng: Double): Deferred<Response<List<Business>>>

    @GET("businesses/{id}")
    fun getVenuesDetail(@Path("id") id: Int): Deferred<Response<Business>>

}