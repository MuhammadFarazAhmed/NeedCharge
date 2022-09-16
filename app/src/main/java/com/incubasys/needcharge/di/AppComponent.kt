package com.incubasys.needcharge.di

import android.app.Application
import com.incubasys.needcharge.NeedChargeApplication
import com.incubasys.needcharge.di.activitylevel.ActivityBuilderModule
import com.incubasys.needcharge.di.scope.AppScope
import com.incubasys.needcharge.di.scope.Base
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient

@AppScope
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBuilderModule::class, AppModule::class])
interface AppComponent : AndroidInjector<NeedChargeApplication> {

    override fun inject(instance: NeedChargeApplication)

    @Base
    fun okhttpClient(): OkHttpClient

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}
