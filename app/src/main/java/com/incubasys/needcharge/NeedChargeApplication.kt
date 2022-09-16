package com.incubasys.needcharge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.Observable
import androidx.multidex.MultiDex
import com.incubasys.needcharge.di.AppComponent
import com.incubasys.needcharge.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import incubasys.needcharge.authentication.ui.AuthActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.home.ui.HomeActivity
import incubasys.needcharge.onboarding.ui.OnboardingActivity
import incubasys.needcharge.profileandsettings.ui.UserActivity
import incubasys.needcharge.repositories.remote.client.SessionAuth
import incubasys.needcharge.repositories.utils.PreferencesHelper
import incubasys.needcharge.splash.ui.SplashActivity
import javax.inject.Inject

class NeedChargeApplication : DaggerApplication(), Navigator {

    companion object {
        private var application: NeedChargeApplication? = null
        fun getInstance(): NeedChargeApplication? {
            return application
        }
    }

    private var appComponent: AppComponent? = null

    @Inject
    lateinit var helper: PreferencesHelper

    private val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            setHeaders()
        }
    }

    override fun onCreate() {
        super.onCreate()
        //Fabric.with(this, Crashlytics())
        application = this
        helper.authHeaders.addOnPropertyChangedCallback(callback)
        setHeaders()
    }


    fun setHeaders() {
        appComponent?.okhttpClient()?.interceptors?.let {
            it.forEach { interceptor ->
                if (interceptor is SessionAuth) {
                    interceptor.setAuthHeader(helper.getAuthHeaders())
                }
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent?.inject(this)
        return appComponent as AppComponent
    }

    //  private val isLoggedInCallback = Observer<Boolean> { setHeaders() }
    override fun startModule(activity: Activity, modules: Navigator.Modules, bundle: Bundle?, startForResult: Int?) {
        val intent = Intent()
        when (modules) {
            Navigator.Modules.SPLASH -> intent.setClass(activity, SplashActivity::class.java)
            Navigator.Modules.ONBOARDING -> intent.setClass(activity, OnboardingActivity::class.java)
            Navigator.Modules.HOME -> intent.setClass(activity, HomeActivity::class.java)
            Navigator.Modules.AUTH -> intent.setClass(activity, AuthActivity::class.java)
            Navigator.Modules.PROFILE_AND_SETTINGS -> intent.setClass(activity, UserActivity::class.java)
            Navigator.Modules.ORDERS_AND_PAYMENT -> intent.setClass(activity, UserActivity::class.java)
        }
        if (bundle != null) {
            intent.putExtra(Navigator.EXTRAS, bundle)
        }
        if (startForResult == null) {
            activity.startActivity(intent)
        } else {
            activity.startActivityForResult(intent, startForResult)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        helper.authHeaders.removeOnPropertyChangedCallback(callback)
    }
}