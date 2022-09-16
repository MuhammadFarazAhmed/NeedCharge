package com.incubasys.needcharge.di.activitylevel

import androidx.lifecycle.ViewModelProvider
import incubasys.needcharge.base.vm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {
    // Here are the ViewModules used by any Module and Factory for all ViewModules

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    //You are able to declare ViewModelProvider.Factory dependency in another module. For example in ApplicationModule.
}
