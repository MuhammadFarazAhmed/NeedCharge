package com.incubasys.needcharge.home

import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.repository.VenueRepository
import incubasys.needcharge.datainterfaces.usecases.HomeUsecase
import incubasys.needcharge.repositories.VenueRepositoryImp
import incubasys.needcharge.repositories.usecases.HomeUsecaseImp
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper

@Module
class HomeModule {


    @Provides
    fun provideHomeUsecase(
            preferencesHelper: PreferencesHelper,repository: VenueRepository,
            parseErrors: ParseErrors
    ): HomeUsecase =
            HomeUsecaseImp(preferencesHelper,repository, parseErrors)
}