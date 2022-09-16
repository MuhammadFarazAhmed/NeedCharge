package incubasys.needcharge.home.vm

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.app.Application
import android.graphics.Color
import android.view.animation.LinearInterpolator
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
/*import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient*/
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.base.utils.SingleLiveEvent
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.HomeUsecase
import incubasys.needcharge.home.BuildConfig
import incubasys.needcharge.home.R
import incubasys.needcharge.home.data.Menu
import incubasys.needcharge.home.data.VenueMapItem
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeViewModel @Inject constructor(application: Application, private val homeUsecase: HomeUsecase) : BaseViewModel(application) {

    var mapMoved: Boolean = false
    val menuList = MutableLiveData<MutableList<Menu>>(ArrayList())
    val mapsCenterPoint = MutableLiveData<LatLng>()
    val startPoint = MutableLiveData<LatLng>()
    val destinationItem = MutableLiveData<VenueMapItem>()
    val venueListSmall = MutableLiveData<List<VenueItem>>(ArrayList())
    val venueMapsList = MutableLiveData<List<VenueMapItem>>(ArrayList())
    val venueListBig = MutableLiveData<List<VenueItem>>(ArrayList())
    private val currentLocation = SingleLiveEvent<LatLng>()
    internal var currentLocationMarker: Marker? = null
    internal var startPositionMarker: Marker? = null

    private val currentPathLatLng = ArrayList<LatLng>()
    var lightClrLine: Polyline? = null
    var darkClrLine: Polyline? = null
    var startPointMarker: Marker? = null
    var currentLineOverlay: Marker? = null
    var lineAnimator: ValueAnimator = ValueAnimator.ofInt(0, 100)
    var placesClient: PlacesClient? = null

    private val isLoggedInCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            setupMenuList()
        }
    }

    fun getCurrentLatLng(): SingleLiveEvent<LatLng> {
        return currentLocation
    }

    fun setCurrentLatLng(currentLatLng: LatLng) {
        this.currentLocation.value = currentLatLng
        loadMainList(currentLatLng)
    }

    init {
        Places.initialize(application, BuildConfig.GoolgeAPIKey)
        placesClient = Places.createClient(application)
        homeUsecase.isLoggedIn().addOnPropertyChangedCallback(isLoggedInCallback)
        setupMenuList()
    }

    private fun setupMenuList() {
        val tempList = mutableListOf<Menu>()
        if (isLoggedIn()) {
            tempList.add(Menu(1, R.drawable.ic_profile, getString(R.string.my_profile)))
        } else {
            tempList.add(Menu(1, R.drawable.ic_profile, getString(R.string.login)))
        }
        tempList.add(Menu(2, R.drawable.ic_order, getString(R.string.past_purchases)))
        tempList.add(Menu(3, R.drawable.ic_learn, getString(R.string.learn_how_to)))
        tempList.add(Menu(4, R.drawable.ic_payment_, getString(R.string.payment_details)))
        tempList.add(Menu(5, R.drawable.ic_settings, getString(R.string.settings)))
        menuList.value = tempList
    }

    fun loadListWhenMapIsIdle(currentMapCenter: LatLng) {
        viewModelScope.launch {
            homeUsecase.getVenueList({

            }, { venueList ->
                val mapsList = ArrayList<VenueMapItem>()
                venueList.forEach {
                    mapsList.add(VenueMapItem(it))
                }
                venueMapsList.value = mapsList
                venueListSmall.value = venueList
            }, {

            }, currentMapCenter.latitude, currentMapCenter.longitude)
        }
    }


    fun loadDirections(onLoading: (boolean: Boolean) -> Unit = {},
                       onSuccess: (blackLineOpt: PolylineOptions,
                                   greyLineOpt: PolylineOptions,
                                   startMarker: MarkerOptions,
                                   lineMarker: MarkerOptions) -> (Unit),
                       onError: (message: Message) -> Unit, venueLatLng: LatLng, currentMapCenter: LatLng) {
        viewModelScope.launch {
            homeUsecase.getDirections(onLoading, { route: List<LatLng>, center: LatLng, _: String ->
                currentPathLatLng.clear()
                currentPathLatLng.addAll(route)
                drawPolyline(center, onSuccess)
            }, onError, currentMapCenter, venueLatLng)
        }
    }


    fun loadMainList(currentLoc: LatLng) {
        viewModelScope.launch {
            homeUsecase.getVenueList({
            }, {
                venueListBig.value = it
            }, {
            }, currentLoc.latitude, currentLoc.longitude)
        }
    }


    fun isLoggedIn() = homeUsecase.isLoggedIn().get()

    override fun onCleared() {
        homeUsecase.isLoggedIn().removeOnPropertyChangedCallback(isLoggedInCallback)
        super.onCleared()
    }

    private fun drawPolyline(center: LatLng, onSuccess: (lightClrLineOpt: PolylineOptions,
                                                         darkClrLineOpt: PolylineOptions,
                                                         startMarker: MarkerOptions,
                                                         lineMarker: MarkerOptions) -> (Unit)) {
        val lightClrLineOption = PolylineOptions()
        lightClrLineOption.width(10f)
        lightClrLineOption.color(Color.parseColor("#A89CFF"))
        lightClrLineOption.startCap(SquareCap())
        lightClrLineOption.endCap(SquareCap())
        lightClrLineOption.jointType(ROUND)

        val startMarker = MarkerOptions().position(startPoint.value!!)
        val lineMarker = MarkerOptions().position(center)

        val darkClrLineOption = PolylineOptions()
        darkClrLineOption.width(10f)
        darkClrLineOption.color(Color.parseColor("#6B55FE"))
        darkClrLineOption.startCap(SquareCap())
        darkClrLineOption.endCap(SquareCap())
        darkClrLineOption.jointType(ROUND)

        animatePolyLine()
        onSuccess(lightClrLineOption, darkClrLineOption, startMarker, lineMarker)
    }


    private fun animatePolyLine() {
        lineAnimator = ValueAnimator.ofInt(0, 100)
        lineAnimator.repeatCount = ValueAnimator.INFINITE
        lineAnimator.repeatMode = ValueAnimator.RESTART
        lineAnimator.duration = 1500
        lineAnimator.interpolator = LinearInterpolator()
        lineAnimator.addUpdateListener { listener ->
            val latLngList = lightClrLine?.points
            val initialPointSize = latLngList?.size
            val animatedValue = listener.animatedValue as Int
            val newPoints = animatedValue * currentPathLatLng.size / 100

            if (initialPointSize != null) {
                if (initialPointSize < newPoints) {
                    latLngList.addAll(currentPathLatLng.subList(initialPointSize, newPoints))
                    lightClrLine?.points = latLngList
                }
            }
        }

        lineAnimator.addListener(polyLineAnimationListener)
        lineAnimator.start()

    }

    private var polyLineAnimationListener: AnimatorListener = object : AnimatorListener {
        override fun onAnimationStart(animator: Animator) {

            //addMarker(currentPathLatLng[currentPathLatLng.size - 1])
        }

        override fun onAnimationEnd(animator: Animator) {

            /* val blackLatLng = lightClrLine?.points
             val greyLatLng = darkClrLine?.points

             if (greyLatLng != null) {
                 greyLatLng.clear()
                 blackLatLng?.let { greyLatLng.addAll(it) }
             }
             if (blackLatLng != null) {
                 blackLatLng.clear()
             }

             lightClrLine?.points = blackLatLng
             darkClrLine?.points = greyLatLng

             lightClrLine?.zIndex = 2f*/
        }

        override fun onAnimationCancel(animator: Animator) {

        }

        override fun onAnimationRepeat(animator: Animator) {

            val blackLatLng = lightClrLine?.points
            val greyLatLng = darkClrLine?.points

            if (greyLatLng != null) {
                greyLatLng.clear()
                blackLatLng?.let { greyLatLng.addAll(it) }
            }
            if (blackLatLng != null) {
                blackLatLng.clear()
            }

            lightClrLine?.points = blackLatLng
            darkClrLine?.points = greyLatLng

            lightClrLine?.zIndex = 2f
        }
    }

}
