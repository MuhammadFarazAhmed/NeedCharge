package com.incubasys.needcharge.auth

import com.incubasys.needcharge.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.*
import incubasys.needcharge.repositories.usecases.*
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper

@Module
class AuthModule {
    @Provides
    @ActivityScope
    fun provideAuthUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): AuthUsecase =
            AuthUsecaseImp(authRepository, preferencesHelper, parseErrors)

    @Provides
    fun provideLoginUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): LoginUsecase =
            LoginUsecaseImp(authRepository, preferencesHelper, parseErrors)

    @Provides
    fun provideSignupUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): SignupUsecase =
            SignupUsecaseImp(authRepository, preferencesHelper, parseErrors)

    @Provides
    fun provideForogotPasswordUsecase(
            authRepository: AuthRepository,
            parseErrors: ParseErrors
    ): ForgotPasswordUsecase =
            ForgotPasswordUsecaseImp(authRepository, parseErrors)

    @Provides
    fun provideVerificationUsecase(
            authRepository: AuthRepository,
            preferencesHelper: PreferencesHelper,
            parseErrors: ParseErrors
    ): VerificationUsecase =
            VerificationUsecaseImp(authRepository, preferencesHelper, parseErrors)

    @Provides
    fun provideChangePasswordUsecase(
            authRepository: AuthRepository,
            parseErrors: ParseErrors
    ): ChangePasswordUsecase =
            ChangePasswordUsecaseImp(authRepository, parseErrors)
}