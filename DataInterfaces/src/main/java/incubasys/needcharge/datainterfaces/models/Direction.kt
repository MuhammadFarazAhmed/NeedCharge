package incubasys.needcharge.datainterfaces.models

/**
 * Created by SamKazmi on 8/24/2017.
 */

data class Direction(val status: String,
                     val routes: List<Route>,
                     val error_message: String) {

    data class Route(val legs: List<Legs>, val overview_polyline: OverviewPolyline)


    data class Legs(
            val distance: TextValue,
            val duration: TextValue,
            val duration_in_traffic: TextValue,
            val start_address: String,
            val end_address: String,
            val start_location: LatLng,
            val end_location: LatLng,
            val steps: List<Step>)

    data class TextValue(val text: String, val value: String)

    data class Step(val polyline: OverviewPolyline,
                    val distance: TextValue,
                    val duration: TextValue,
                    val end_location: LatLng,
                    val html_instructions: String,
                    val start_location: LatLng,
                    val travel_mode: String)

    data class OverviewPolyline(val points: String)

    data class LatLng(val lat: String, val lng: String)
}
