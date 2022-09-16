package incubasys.needcharge.repositories.usecases

import com.google.android.gms.maps.model.LatLng
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.repository.VenueRepository
import incubasys.needcharge.datainterfaces.usecases.HomeUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeUsecaseImp @Inject constructor(
        private val preferencesHelper: PreferencesHelper,
        private val venueRepository: VenueRepository,
        parseErrors: ParseErrors
) :
        BaseUsecase(parseErrors), HomeUsecase {
    override suspend fun getDirections(onLoading: (boolean: Boolean) -> Unit, onSuccess: (polyLine: List<LatLng>, center: LatLng, stringToShow: String) -> Unit, onError: (message: Message) -> Unit, origin: LatLng, destination: LatLng) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                venueRepository.getDirections("${origin.latitude},${origin.longitude}",
                        "${destination.latitude},${destination.longitude}").await()
            }
            if (result.isSuccessful) {
                val polyLine = result.body()?.let { direction ->
                    var polyLine: List<LatLng>? = null
                    if (direction.status == "OK") {
                        polyLine = withContext(Dispatchers.IO) {
                            // val polyLine = ArrayList<LatLng>()
                            /*val polyLine =*/ decodePoly(direction.routes[0].overview_polyline.points)

                            //  val routes = ArrayList<List<HashMap<String, String>>>()
                            /*  direction.routes.forEach { route ->
                                 //  val path = ArrayList<HashMap<String, String>>()
                                  route.legs.forEach { legs ->
                                      legs.steps.forEach { step ->
                                          val list = decodePoly(step.polyline.points)
                                          list.forEach { latLng ->
                                              *//*val hm = HashMap<String, String>()
                                            hm["lat"] = latLng.latitude.toString()
                                            hm["lng"] = latLng.longitude.toString()
                                            path.add(hm)*//*
                                            polyLine.add(latLng)
                                        }
                                    }
                                }
                                // routes.add(path)
                            }*/

                            /* routes.forEach { route ->
                                 val points = ArrayList<LatLng>()
                                 route.forEach { path ->
                                     val lat = path["lat"]?.toDouble()
                                     val lng = path["lng"]?.toDouble()
                                     ifLet(lat, lng){ (latN, lngN)->
                                         points.add(LatLng(latN, lngN))
                                     }
                                 }
                                 polyLine.addAll(points)
                             }*/

                            //  polyLine
                        }
                    }
                    polyLine
                }
                onLoading(false)
                if (polyLine != null) {
                    onSuccess(polyLine, polyLine[polyLine.size / 2], "")
                } else {
                    onError(Message(message = "Unable to draw path"))
                }
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }

    override suspend fun getVenueList(onLoading: (boolean: Boolean) -> Unit, onSuccess: (list: List<VenueItem>) -> Unit, onError: (message: Message) -> Unit, lat: Double, lng: Double) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                venueRepository.getVenueList(lat, lng).await()
            }
            if (result.isSuccessful) {
                onLoading(false)
                onSuccess(withContext(Dispatchers.IO) {
                    val list = mutableListOf<VenueItem>()
                    result.body()?.let { businessesList ->
                        businessesList.forEach { business ->
                            val item = VenueItem(business.id, business.title, business.banner?.url
                                    ?: "", LatLng(business.lat.toDouble(), business.lng.toDouble()), false, null, mutableListOf(), business.businessType, business.distance?.toDouble()
                                    ?: 0.0, business.duration?.toDouble() ?: 0.0)
                            business.businessHours?.let {
                                item.setBusinessHours(it)
                            }
                            list.add(item)
                        }
                    }
                    list
                })
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }


    override suspend fun getVenueDetail(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: VenueItem) -> Unit, onError: (message: Message) -> Unit, id: Int) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                venueRepository.getVenueDetail(id).await()
            }
            if (result.isSuccessful) {
                val venueItem = withContext(Dispatchers.IO) {
                    var venueItem: VenueItem? = null
                    result.body()?.let { business ->
                        venueItem = VenueItem(business.id, business.title, business.banner?.url
                                ?: "", LatLng(business.lat.toDouble(), business.lng.toDouble()), false, null, mutableListOf(), business.businessType, business.distance?.toDouble()
                                ?: 0.0, business.duration?.toDouble() ?: 0.0)
                        business.businessHours?.let {
                            venueItem?.setBusinessHours(it)
                        }
                    }
                    venueItem
                }
                onLoading(false)
                if (venueItem != null){
                    onSuccess(venueItem)
                }else{
                    onError(Message(0,"Unable to parse data"))
                }
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }

    override fun isLoggedIn() = preferencesHelper.isLoggedIn
    override fun getUser() = preferencesHelper.getUser()


    private fun decodePoly(encoded: String): List<LatLng> {

        val poly = java.util.ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
        if (elements.all { it != null }) {
            closure(elements.filterNotNull())
        }
    }
}