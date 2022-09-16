package incubasys.needcharge.home.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doAfterTextChanged
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ui.IconGenerator
import incubasys.needcharge.base.*
import incubasys.needcharge.base.utils.Constants.CAMERA_ZOOM_CURRENT_LOC
import incubasys.needcharge.base.utils.Constants.MAX_ZOOM
import incubasys.needcharge.home.R
import incubasys.needcharge.home.adapter.PlacesAutoCompleteAdapter
import incubasys.needcharge.home.adapter.VenueAdapter
import incubasys.needcharge.home.adapter.VenuePagerAdapter
import incubasys.needcharge.home.callback.HomeCallback
import incubasys.needcharge.home.callback.HomeViewCallback
import incubasys.needcharge.home.data.VenueMapItem
import incubasys.needcharge.home.databinding.HomeFragmentBinding
import incubasys.needcharge.home.utils.VenueClusterRenderer
import incubasys.needcharge.home.vm.HomeViewModel


class HomeFragment : BaseFragment(), OnMapReadyCallback, HomeViewCallback, RecyclerViewCallback, PlacesAutoCompleteAdapter.ClickListener {

    override fun onPlaceClicked(place: Place) {
        moveMapToLocation(place.latLng?.latitude?.let { place.latLng?.longitude?.let { it1 -> LatLng(it, it1) } })
        binding.rvAutoCompletePredictions.visibility = View.GONE
    }

    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<VenueMapItem>
    private val vm: HomeViewModel by lazy {
        (activity as HomeActivity).getHomeViewModel()
    }

    private lateinit var binding: HomeFragmentBinding

    private val smallAdapter by lazy {
        VenuePagerAdapter(callback = this, mContext = requireContext())
    }

    private val bigAdapter = VenueAdapter(this)

    private val TRANSLATE_Y by lazy {
        resources.getDimension(R.dimen.max_translate_value)
    }

    private val MARGIN by lazy {
        resources.getDimension(R.dimen.venue_margin_bottom)
    }

    private var callback: HomeCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeCallback) {
            callback = context
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.rvSmallList.adapter = smallAdapter
        binding.rvBigList.adapter = bigAdapter
        binding.rvBigList.visibility = View.GONE
        isBigListShowin = false

        vm.venueListBig.observe(this, Observer {
            bigAdapter.submitList(it)
        })

        binding.rvAutoCompletePredictions.layoutManager = LinearLayoutManager(requireContext())
        val adapter = vm.placesClient?.let { PlacesAutoCompleteAdapter(requireContext(), it) }
        binding.rvAutoCompletePredictions.adapter = adapter
        adapter?.setClickListener(this)
        TransitionManager.beginDelayedTransition(binding.rvAutoCompletePredictions)

        binding.etSearch.doAfterTextChanged {
            if (it.toString().isNotBlank()) {
                adapter?.filter?.filter(it.toString())
                if (binding.rvAutoCompletePredictions.visibility == View.GONE) {
                    binding.rvAutoCompletePredictions.visibility = View.VISIBLE
                }
            } else {
                if (binding.rvAutoCompletePredictions.visibility == View.VISIBLE) {
                    binding.rvAutoCompletePredictions.visibility = View.GONE
                }
            }
        }
        callback?.requestLocation()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        DrawableCompat.setTint(binding.bCurrentLocation.drawable, Color.WHITE)
        binding.rvSmallList.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        binding.rvBigList.layoutManager = LinearLayoutManager(requireContext())

        vm.venueListSmall.observe(this, Observer {
            smallAdapter.updateList(it)
        })
        hideSmallList()

        vm.destinationItem.observe(this, Observer {
            if (it != null) {
                if (vm.startPoint.value == null) {
                    vm.startPoint.value = vm.getCurrentLatLng().value
                }
                vm.loadDirections(onLoading(), { blackLineOptions: PolylineOptions,
                                                 greyLineOption: PolylineOptions,
                                                 startMarker: MarkerOptions,
                                                 lineMarker: MarkerOptions ->
                    vm.darkClrLine = mMap.addPolyline(greyLineOption)
                    vm.lightClrLine = mMap.addPolyline(blackLineOptions)

                    vm.startPositionMarker = mMap.addMarker(startMarker
                            .position(vm.getCurrentLatLng().value!!)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point_marker)))

                    val iconGenerator = IconGenerator(context)
                    iconGenerator.setBackground(null)
                    val mapView = LayoutInflater.from(context).inflate(R.layout.map_line_overlay, null)
                    val textView = mapView.findViewById<TextView>(R.id.tvMapLineOverlayText)
                    textView.text = getString(R.string.km_with_value,
                            vm.destinationItem.value?.venueItem?.distance ?: 0.0)
                            .plus(" (")
                            .plus(resources.getQuantityString(R.plurals.duration,
                                    (vm.destinationItem.value?.venueItem?.distance?.toInt() ?: 0),
                                    vm.destinationItem.value?.venueItem?.distance).plus(")"))
                    iconGenerator.setContentView(mapView)
                    vm.currentLineOverlay = mMap.addMarker(lineMarker.anchor(0.5f, 1.5f).zIndex(5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())))


                }, {
                    showErrorMessage(it.message)
                }, it.venueItem.location, vm.startPoint.value!!)
            }
        })
    }

    fun loadBitmapFromView(): Bitmap {
        val view = LayoutInflater.from(context).inflate(R.layout.map_line_overlay, null)
        val textView = view.findViewById<TextView>(R.id.tvMapLineOverlayText)
        textView.text = getString(R.string.km_with_value,
                vm.destinationItem.value?.venueItem?.distance ?: 0.0)
                .plus(" (")
                .plus(resources.getQuantityString(R.plurals.duration,
                        (vm.destinationItem.value?.venueItem?.distance?.toInt() ?: 0),
                        vm.destinationItem.value?.venueItem?.distance))


        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

    private var isSmallListShowin = false
    private var isBigListShowin = false
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        mMap.setMaxZoomPreference(MAX_ZOOM)
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isCompassEnabled = false
        mMap.uiSettings.isMapToolbarEnabled = false

        context?.let { context ->
            mClusterManager = ClusterManager(context, mMap)
            mMap.setOnMarkerClickListener(mClusterManager)
            mMap.setOnInfoWindowClickListener(mClusterManager)
            mClusterManager.setOnClusterItemInfoWindowClickListener {
            }
            mClusterManager.setOnClusterItemClickListener {
                if (vm.destinationItem.value == null && !isSmallListShowin) {
                    showSmallList()
                    vm.destinationItem.value = it
                } else if (vm.destinationItem.value?.equals(it)!!) {
                    hideSmallList()
                    vm.darkClrLine?.remove()
                    vm.lightClrLine?.remove()
                    vm.startPointMarker?.remove()
                    vm.currentLineOverlay?.remove()
                    vm.lineAnimator.cancel()
                    vm.startPositionMarker?.remove()

                    vm.destinationItem.value = null
                } else {
                    vm.darkClrLine?.remove()
                    vm.lightClrLine?.remove()
                    vm.startPointMarker?.remove()
                    vm.currentLineOverlay?.remove()
                    vm.lineAnimator.cancel()
                    vm.startPositionMarker?.remove()

                    binding.rvSmallList.post {
                        vm.venueMapsList.value?.indexOf(it)?.let { index -> binding.rvSmallList.setCurrentItem(index, true) }
                    }
                    vm.destinationItem.value = it
                }
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
                false
            }
            mClusterManager.renderer = VenueClusterRenderer(context, mMap, mClusterManager)
            // mClusterManager.markerCollection?.setOnInfoWindowAdapter(infoWindow)
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            if (context != null) {
                val success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.map_style))

                if (!success) {
                    Log.e(TAG, "Style parsing failed.")
                }
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
        mMap.setOnCameraIdleListener {
            val previousMapCenter = vm.mapsCenterPoint.value
            vm.mapsCenterPoint.value = mMap.cameraPosition.target
            if (previousMapCenter == null || SphericalUtil.computeDistanceBetween(vm.mapsCenterPoint.value, previousMapCenter) > 2000) {
                vm.mapsCenterPoint.value?.let {
                    vm.loadListWhenMapIsIdle(it)
                }
            }
        }
        mMap.setOnCameraMoveStartedListener {
            vm.mapMoved = true
        }
        // moveMapToLocation(LatLng(-25.005881, 134.321670))

        vm.venueMapsList.observe(this, Observer {
            mClusterManager.clearItems()
            mClusterManager.addItems(it)
            mClusterManager.cluster()
        })
    }

    private fun showSmallList() {
        isSmallListShowin = true

        binding.rvSmallList.animate().translationY(0f).setStartDelay(130).setInterpolator(FastOutSlowInInterpolator()).start()
        binding.bList.animate().translationY(0f).setInterpolator(FastOutSlowInInterpolator()).start()
        binding.bCurrentLocation.animate().translationY(0f).setInterpolator(FastOutSlowInInterpolator()).start()
        binding.rvSmallList.post {
            binding.rvSmallList.setCurrentItem((0..14).random(), true)
        }
    }

    private fun hideSmallList() {
        isSmallListShowin = false

        binding.rvSmallList.animate().translationY(TRANSLATE_Y).setInterpolator(FastOutSlowInInterpolator()).start()
        binding.bList.animate().translationY(TRANSLATE_Y - MARGIN).setInterpolator(FastOutSlowInInterpolator()).setStartDelay(130).start()
        binding.bCurrentLocation.animate().translationY(TRANSLATE_Y - MARGIN).setInterpolator(FastOutSlowInInterpolator()).setStartDelay(130).start()
    }

    override fun onCurrentLocationButtonClicked() {
        vm.mapMoved = false
        if (isSmallListShowin) {
            hideSmallList()
        }
        if (isBigListShowin) {
            hideBigList()
        }
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)

        // TODO clear map center position

        if (vm.getCurrentLatLng().value == null) {
            callback?.requestLocation()
        } else {
            moveMapToLocation(vm.getCurrentLatLng().value)
        }
    }

    override fun onListButtonClicked() {
        if (!isBigListShowin) {
            showBigList()
        } else {
            hideBigList()
        }
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
    }

    private fun showBigList() {
        if (isSmallListShowin) {
            hideSmallList()
        }
        binding.rvBigList.visibility = View.VISIBLE
        isBigListShowin = true
        binding.bList.setIconResource(R.drawable.ic_map_pin)
        binding.bList.setText(R.string.map)
    }

    private fun hideBigList() {
        binding.rvBigList.visibility = View.GONE
        isBigListShowin = false
        binding.bList.setIconResource(R.drawable.ic_list)
        binding.bList.setText(R.string.list)
    }

    override fun onMenuButtonClicked() {
        callback?.openMenu()
    }

    override fun onHowToButtonClicked() {
        callback?.showOnboardingScreen()
    }

    override fun onListItemClicked(position: Int) {
        //TODO get the id of this Position
        //TODO fetch the details from the id
        //TODO open BusinessDetailFragment
    }


    @SuppressLint("MissingPermission")
    fun onLocationChanged(latLng: LatLng) {
        vm.setCurrentLatLng(latLng)

        vm.currentLocationMarker?.remove()
        vm.currentLocationMarker = mMap.addMarker(MarkerOptions()
                .position(latLng)
                .zIndex(6f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location_marker)))

        if (!vm.mapMoved) {
            moveMapToLocation(latLng)
        }
    }

    private fun moveMapToLocation(latLng: LatLng?) {
        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM_CURRENT_LOC))
        }
    }
}
