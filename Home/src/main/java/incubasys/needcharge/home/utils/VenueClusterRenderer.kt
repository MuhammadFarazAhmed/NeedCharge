package incubasys.needcharge.home.utils

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import incubasys.needcharge.home.R
import incubasys.needcharge.home.data.VenueMapItem
import android.graphics.Bitmap
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ui.IconGenerator


class VenueClusterRenderer(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<VenueMapItem>) : DefaultClusterRenderer<VenueMapItem>(context, map, clusterManager) {

    private val mClusterIconGenerator = IconGenerator(context)

    override fun shouldRenderAsCluster(cluster: Cluster<VenueMapItem>): Boolean {
        return cluster.size > 2
    }

    override fun onClusterItemRendered(clusterItem: VenueMapItem?, marker: Marker?) {
        marker?.tag = clusterItem?.venueItem?.id
        super.onClusterItemRendered(clusterItem, marker)
    }

    override fun onBeforeClusterItemRendered(item: VenueMapItem, markerOptions: MarkerOptions) {
        //  0 => Restuarant 1 => Bar 2 => Gas Station 3 => Hospital 4 => Beauty Salon 5 => Cinema 6 => Motel
        val icon = when (item.venueItem.type) {
            0 -> BitmapDescriptorFactory.fromResource(R.drawable.restuarant)
            1 -> BitmapDescriptorFactory.fromResource(R.drawable.bar)
            2 -> BitmapDescriptorFactory.fromResource(R.drawable.gas_station)
            3 -> BitmapDescriptorFactory.fromResource(R.drawable.hospital_closed)
            4 -> BitmapDescriptorFactory.fromResource(R.drawable.beauty_salon_closed)
            5 -> BitmapDescriptorFactory.fromResource(R.drawable.cinema)
            6 -> BitmapDescriptorFactory.fromResource(R.drawable.motel_closed)
            else -> BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_pin)
        }
        markerOptions.icon(icon)
    }

    /*override fun onBeforeClusterRendered(cluster: Cluster<VenueMapItem>?, markerOptions: MarkerOptions?) {
        // super.onBeforeClusterRendered(cluster, markerOptions)


        val clusterIcon = ContextCompat.getDrawable(context,R.drawable.ic_marker_pin)
        clusterIcon?.setColorFilter(ContextCompat.getColor(context,android.R.color.black), PorterDuff.Mode.SRC_ATOP)

        mClusterIconGenerator.setBackground(clusterIcon)

        //modify padding for one or two digit numbers
        if (cluster?.size?.coerceAtLeast(10) == cluster?.size) {
            mClusterIconGenerator.setContentPadding(40, 20, 0, 0)
        } else {
            mClusterIconGenerator.setContentPadding(30, 20, 0, 0)
        }

        //mClusterIconGenerator.setContentView()
        val icon = mClusterIconGenerator.makeIcon(cluster?.size.toString())
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }*/

}