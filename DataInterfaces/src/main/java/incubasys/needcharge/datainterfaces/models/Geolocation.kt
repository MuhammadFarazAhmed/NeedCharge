package incubasys.needcharge.datainterfaces.models

/**
 * Created by SamKazmi on 3/15/2018.
 */

data class Geolocation(val status: String? = null) {

    val results: List<Result>? = null
    val error_message: String? = null


    inner class Result {
        val address_components: List<Components>? = null
        val formatted_address: String? = null
        val geometry: Geometry? = null
        val place_id: String? = null
        val types: Array<String>? = null
    }


    inner class Components {
        val long_name: String? = null
        val short_name: String? = null
        val types: Array<String>? = null
    }

    inner class Geometry {
        val location: LatLng? = null
        val bounds: ViewPort? = null
        val viewport: ViewPort? = null
        val location_type: String? = null
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
