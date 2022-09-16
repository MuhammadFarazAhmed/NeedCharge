package incubasys.needcharge.base.utils

import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.maps.model.LatLng

interface LocationUpdateCallback {

    fun onLocationChanged(latLng: LatLng)
    fun onLocationSettingsSuccess(settingsStates: LocationSettingsStates)
    fun onSettingsFailure(e: Exception)
}
