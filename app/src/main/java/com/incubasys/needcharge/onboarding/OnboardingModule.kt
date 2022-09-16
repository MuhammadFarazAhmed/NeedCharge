package com.incubasys.needcharge.onboarding

import com.incubasys.needcharge.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.usecases.OnboardingUsecase
import incubasys.needcharge.datainterfaces.usecases.SplashUsecase
import incubasys.needcharge.repositories.usecases.OnboardingUsecaseImp
import incubasys.needcharge.repositories.usecases.SplashUsecaseImp
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper

@Module
class OnboardingModule {

    @Provides
    @ActivityScope
    fun provideOnBoardingUsecase(
        preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
    ): OnboardingUsecase =
        OnboardingUsecaseImp(preferencesHelper, parseErrors)

}