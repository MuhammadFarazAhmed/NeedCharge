package com.incubasys.needcharge.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import incubasys.needcharge.authentication.ui.*
import incubasys.needcharge.authentication.vm.*
import incubasys.needcharge.base.vm.ViewModelKey

@Suppress("unused")
@Module
abstract class AuthBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSignupViewModel(viewModel: SignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel::class)
    abstract fun bindVerificationViewModel(viewModel: VerificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TermsViewModel::class)
    abstract fun bindTermsViewModel(viewModel: TermsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    abstract fun contributeSignupFragment(): SignupFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeVerificationFragment(): VerificationFragment

    @ContributesAndroidInjector
    abstract fun contributeTermsFragment(): TermsFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment
}
