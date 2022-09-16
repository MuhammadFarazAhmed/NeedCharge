package incubasys.needcharge.home.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import incubasys.needcharge.datainterfaces.VenueItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VenueMapItem(val venueItem: VenueItem) : Parcelable, ClusterItem {
    override fun getPosition(): LatLng {
        return venueItem.location
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getSnippet(): String {
        return ""
    }
}
