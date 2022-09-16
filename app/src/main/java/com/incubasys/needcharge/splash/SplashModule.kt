package com.incubasys.needcharge.splash

import com.incubasys.needcharge.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.SplashUsecase
import incubasys.needcharge.repositories.usecases.SplashUsecaseImp
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper

@Module
class SplashModule {

    @Provides
    @ActivityScope
    fun provideSplashUsecase(
        authRepository: AuthRepository,
        preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
    ): SplashUsecase =
        SplashUsecaseImp(authRepository, preferencesHelper, parseErrors)

}