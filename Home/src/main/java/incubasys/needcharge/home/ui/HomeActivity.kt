package incubasys.needcharge.home.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.maps.model.LatLng
import incubasys.needcharge.base.BaseActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.base.RecyclerViewCallback
import incubasys.needcharge.base.utils.LocationUpdateCallback
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.home.R
import incubasys.needcharge.home.adapter.MenuAdapter
import incubasys.needcharge.home.callback.HomeCallback
import incubasys.needcharge.home.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_DIALOG
import incubasys.needcharge.home.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_SETTING
import incubasys.needcharge.home.ui.LocationSetupDialogFragment.Companion.TYPE_LOCATION_SETTINGS_ON
import incubasys.needcharge.home.utils.LocationHelper
import incubasys.needcharge.home.utils.LocationHelper.Companion.REQUEST_CHECK_LOCATION_SETTINGS
import incubasys.needcharge.home.utils.LocationHelper.Companion.REQUEST_CHECK_PERMISSION
import incubasys.needcharge.home.utils.LocationHelper.Companion.REQUEST_CHECK_PERMISSION_SETTINGS
import incubasys.needcharge.home.vm.HomeViewModel
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_menu.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), RecyclerViewCallback, LocationUpdateCallback, HomeCallback, LocationSetupDialogFragment.Listener {
    override fun openMenu() {
        openDrawer()
    }

    override fun showOnboardingScreen() {
        openOnBoardingModule()
    }

    override fun showVenueDetail(venueItem: VenueItem) {

    }

    override fun requestLocation() {
        if (helper.isLocationPermissionGranted(this)) {
            helper.requestLocationSettings(this)
        } else {
            helper.requestPermission(this, supportFragmentManager)
        }
    }

    override fun onLocationChanged(latLng: LatLng) {
        // helper.removeLocationUpdate()
        val fragment = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment != null) {
            (fragment as HomeFragment).onLocationChanged(latLng)
        }
    }

    override fun onLocationSettingsSuccess(settingsStates: LocationSettingsStates) {
        helper.requestLocationUpdate(applicationContext)
    }

    override fun onSettingsFailure(e: Exception) {
        helper.startOnResultionResult(this, e)
    }

    override fun onPermissionDialogButtonClicked(messageType: Int) {
        when (messageType) {
            TYPE_ALLOW_LOCATION_PERMISSION_DIALOG -> helper.requestPermission(this, supportFragmentManager)
            TYPE_ALLOW_LOCATION_PERMISSION_SETTING -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null))
                /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                startActivityForResult(intent, REQUEST_CHECK_PERMISSION_SETTINGS)
            }
            TYPE_LOCATION_SETTINGS_ON -> helper.requestLocationSettings(this)
        }
    }

    private val helper = LocationHelper(this)
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val vm: HomeViewModel by lazy {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        motionLayout.setScrimColor(Color.TRANSPARENT)
        motionLayout.drawerElevation = 0f
        rvMenu.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        vm.menuList.observe(this, Observer {
            rvMenu.adapter = MenuAdapter(this, it)
        })

        ivClose.setOnClickListener { closeDrawer() }
    }

    fun getHomeViewModel() = vm

    private fun openDrawer() {
        motionLayout.openDrawer(GravityCompat.START, true)
    }

    private fun closeDrawer() {
        motionLayout.closeDrawer(GravityCompat.START, true)
    }

    override fun onListItemClicked(position: Int) {
        var module = Navigator.Modules.AUTH
        var bundle: Bundle? = null
        when (vm.menuList.value?.get(position)?.id) {
            1 -> {
                if (vm.isLoggedIn()) {
                    module = Navigator.Modules.PROFILE_AND_SETTINGS
                    bundle = Bundle()
                    bundle.putInt(Navigator.PROFILE_AND_SETTINGS_ARG_GOTO, 1)
                } else {
                    module = Navigator.Modules.AUTH
                    bundle = null
                }
            }
            2 -> {
                module = Navigator.Modules.ORDERS_AND_PAYMENT
                bundle = null
            }
            3 -> {
                module = Navigator.Modules.ONBOARDING
                bundle = Bundle()
                bundle.putBoolean(Navigator.ONBOARDING_ARG_SHOWNFROM, true)
            }
            4 -> {
                module = Navigator.Modules.ORDERS_AND_PAYMENT
                bundle = null
            }
            5 -> {
                module = Navigator.Modules.PROFILE_AND_SETTINGS
                bundle = Bundle()
                bundle.putInt(Navigator.PROFILE_AND_SETTINGS_ARG_GOTO, 0)
            }
        }
        motionLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                navigator.startModule(this@HomeActivity, module, bundle)
                motionLayout.removeDrawerListener(this)
            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })
        closeDrawer()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus

        if (v != null &&
                (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                v is EditText &&
                !v.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.left - scrcoords[0]
            val y = ev.rawY + v.top - scrcoords[1]

            if (x < v.left || x > v.right || y < v.top || y > v.bottom) {
                v.clearFocus()
                v.isCursorVisible = false
                hideKeyboard(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    private fun openOnBoardingModule() {
        val bundle = Bundle()
        bundle.putBoolean(Navigator.ONBOARDING_ARG_SHOWNFROM, true)
        navigator.startModule(this, Navigator.Modules.ONBOARDING, bundle)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CHECK_PERMISSION) {
            if (helper.onRequestPermissionsResult(grantResults)) {
                helper.requestLocationSettings(this)
            } else {
                helper.showDialogOnPermissionFailed(this, supportFragmentManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_LOCATION_SETTINGS) {
            if (!helper.onActivityResult(resultCode, data)) {
                helper.showDialogOnSettingsCancelled(supportFragmentManager)
            }
        } else if (requestCode == REQUEST_CHECK_PERMISSION_SETTINGS) {
            helper.requestPermission(this, supportFragmentManager)
        }
    }
}
