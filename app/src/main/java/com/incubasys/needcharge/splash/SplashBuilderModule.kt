package com.incubasys.needcharge.splash

import androidx.lifecycle.ViewModel

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import incubasys.needcharge.base.vm.ViewModelKey
import incubasys.needcharge.splash.ui.SplashFragment
import incubasys.needcharge.splash.vm.SplashViewModel

@Suppress("unused")
@Module
abstract class SplashBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

}
