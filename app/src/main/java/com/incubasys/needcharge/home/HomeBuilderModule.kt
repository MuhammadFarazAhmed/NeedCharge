package com.incubasys.needcharge.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import incubasys.needcharge.base.vm.ViewModelKey
import incubasys.needcharge.home.ui.BusinessDetailFragment
import incubasys.needcharge.home.ui.HomeFragment
import incubasys.needcharge.home.vm.HomeViewModel

@Suppress("unused")
@Module
abstract class HomeBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeBusinessDetailFragment(): BusinessDetailFragment
}
