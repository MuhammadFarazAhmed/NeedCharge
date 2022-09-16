package com.incubasys.needcharge.onboarding

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import incubasys.needcharge.base.vm.ViewModelKey
import incubasys.needcharge.onboarding.ui.OnboardingFragment
import incubasys.needcharge.onboarding.viewmodel.OnboardingViewModel

@Suppress("unused")
@Module
abstract class OnboardingBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    abstract fun bindOnBoardingViewModel(viewModel: OnboardingViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeOnboardingFragment(): OnboardingFragment

}
