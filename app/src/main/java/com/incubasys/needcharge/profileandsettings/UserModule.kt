package com.incubasys.needcharge.profileandsettings

import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.ProfileUsecase
import incubasys.needcharge.datainterfaces.usecases.SettingsUsecase
import incubasys.needcharge.repositories.usecases.ProfileUsecaseImp
import incubasys.needcharge.repositories.usecases.SettingsUsecaseImp
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper

@Module
class UserModule {

    @Provides
    fun provideUserUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): SettingsUsecase =
            SettingsUsecaseImp(authRepository, preferencesHelper, parseErrors)



    @Provides
    fun provideProfileUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): ProfileUsecase =
            ProfileUsecaseImp(authRepository, preferencesHelper, parseErrors)
}