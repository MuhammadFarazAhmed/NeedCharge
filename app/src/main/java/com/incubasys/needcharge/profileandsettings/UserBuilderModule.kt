package com.incubasys.needcharge.profileandsettings

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import incubasys.needcharge.authentication.ui.*
import incubasys.needcharge.authentication.vm.*
import incubasys.needcharge.base.vm.ViewModelKey
import incubasys.needcharge.profileandsettings.ui.ProfileFragment
import incubasys.needcharge.profileandsettings.ui.SettingsFragment
import incubasys.needcharge.profileandsettings.ui.UpdateProfileFragment
import incubasys.needcharge.profileandsettings.vm.ProfileViewModel
import incubasys.needcharge.profileandsettings.vm.SettingsViewModel
import incubasys.needcharge.profileandsettings.vm.UpdateProfileViewModel

@Suppress("unused")
@Module
abstract class UserBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateProfileViewModel::class)
    abstract fun bindUpdateProfileViewModel(viewModel: UpdateProfileViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeUpdateProfileFragment(): UpdateProfileFragment

}
