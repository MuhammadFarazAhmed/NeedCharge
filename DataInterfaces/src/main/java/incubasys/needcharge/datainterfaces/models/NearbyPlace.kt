package incubasys.needcharge.datainterfaces.models

/**
 * Created by SamKazmi on 3/15/2018.
 */

data class NearbyPlace(val status: String? = null) {

    val results: List<Result>? = null
    val error_message: String? = null


    inner class Result {
        val icon: String? = null
        val id: String? = null
        val name: String? = null
        val place_id: String? = null
        val rating: Float? = null
        val vicinity: String? = null
        val geometry: Geometry? = null
    }


    inner class Geometry {
        val location: LatLng? = null
        val lng: ViewPort? = null
    }

    inner class ViewPort {
        val northeast: LatLng? = null
        val southwest: LatLng? = null
    }

    inner class LatLng {
        val lat: String? = null
        val lng: String? = null
    }

}
