package com.incubasys.needcharge.di.activitylevel


import com.incubasys.needcharge.auth.AuthBuilderModule
import com.incubasys.needcharge.auth.AuthModule
import com.incubasys.needcharge.profileandsettings.UserBuilderModule
import com.incubasys.needcharge.profileandsettings.UserModule
import com.incubasys.needcharge.di.scope.ActivityScope
import com.incubasys.needcharge.home.HomeBuilderModule
import com.incubasys.needcharge.home.HomeModule
import com.incubasys.needcharge.onboarding.OnboardingBuilderModule
import com.incubasys.needcharge.onboarding.OnboardingModule
import com.incubasys.needcharge.splash.SplashBuilderModule
import com.incubasys.needcharge.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import incubasys.needcharge.authentication.ui.AuthActivity
import incubasys.needcharge.home.ui.HomeActivity
import incubasys.needcharge.onboarding.ui.OnboardingActivity
import incubasys.needcharge.profileandsettings.ui.UserActivity
import incubasys.needcharge.splash.ui.SplashActivity


@Suppress("unused")
@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashBuilderModule::class, SplashModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [OnboardingBuilderModule::class, OnboardingModule::class])
    abstract fun contributeOnboardingActivity(): OnboardingActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeBuilderModule::class, HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AuthBuilderModule::class, AuthModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [UserBuilderModule::class, UserModule::class])
    abstract fun contributeUserActivity(): UserActivity
}