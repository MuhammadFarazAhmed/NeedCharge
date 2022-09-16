package com.incubasys.needcharge.di.activitylevel

import com.incubasys.needcharge.di.scope.Base
import com.incubasys.needcharge.di.scope.Google
import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.repository.VenueRepository
import incubasys.needcharge.repositories.AuthRepositoryImp
import incubasys.needcharge.repositories.VenueRepositoryImp
import incubasys.needcharge.repositories.remote.api.AuthApi
import incubasys.needcharge.repositories.remote.api.GoogleApi
import incubasys.needcharge.repositories.remote.api.VenueApi
import retrofit2.Retrofit

@Module(includes = [ViewModelModule::class])
class RepositoryModule {

    @Provides
    fun provideAuthApi(@Base retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideVenueApi(@Base retrofit: Retrofit): VenueApi = retrofit.create(VenueApi::class.java)

    @Provides
    fun provideGoogleApi(@Google retrofit: Retrofit): GoogleApi = retrofit.create(GoogleApi::class.java)

    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository = AuthRepositoryImp(authApi)

    @Provides
    fun provideVenueRepository(venueApi: VenueApi,googleApi: GoogleApi) : VenueRepository = VenueRepositoryImp(venueApi,googleApi)
}
// Here we provide a common repository that has @AppScope and used by multiple modules(by modules I mean Activity)
