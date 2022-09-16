package incubasys.needcharge.authentication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.authentication.callback.LoginCallback
import incubasys.needcharge.authentication.callback.LoginViewCallack
import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.authentication.databinding.LoginFragmentBinding
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.LoginViewModel
import incubasys.needcharge.base.R
import incubasys.needcharge.base.getDrawable
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.utils.DrawableClickListener
import javax.inject.Inject

class LoginFragment : AuthBaseFragment(), LoginViewCallack {

    companion object {
        private const val ARG = "loginType"
        fun newInstance(signupType: AuthType): LoginFragment {
            val f = LoginFragment()
            val bundle = Bundle()
            bundle.putInt(ARG, signupType.ordinal)
            f.arguments = bundle
            return f
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: LoginFragmentBinding
    private val vm: LoginViewModel by lazy {
        ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }
    private var callback: LoginCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        arguments?.let {
            vm.loginType = AuthType.fromInt(it.getInt(ARG, 0))
        }
        binding.vm = vm
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.etPassword.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etPassword) {
            override fun onDrawableClick(): Boolean {
                val mDrawable = getDrawable(R.drawable.ic_preview)
                if (binding.etPassword.transformationMethod == null) {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod()
                    mDrawable?.colorFilter = null
                } else {
                    binding.etPassword.transformationMethod = null
                    mDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor("#D4DCE1"), PorterDuff.Mode.MULTIPLY)
                }
                mDrawable?.let {
                    binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(binding.etPassword.compoundDrawablesRelative[0], null, mDrawable, null)
                }
                return false
            }
        })
        if (activity != null) {
            binding.etPassword.transformationMethod = PasswordTransformationMethod()
        }

        /*  vm.getError().observe(this, Observer(onErrorSimple()))
          vm.getShowProgress().observe(this, Observer {
              if (it) {
                  showProgress()
              } else {
                  hideProgress()
              }
          })*/
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun onLoginButtonClicked() {
        when (vm.loginType) {
            AuthType.PHONE -> {
                vm.generatePinForLoginWithPhone(onLoading = onLoading(), onSuccess = {
                    if (it) {
                        binding.etPhone.text?.let { phoneNumber ->
                            callback?.showPhoneVerificationForLogin(phoneNumber.toString())
                        }
                    }
                }, onError = onErrorSimple())
            }
            AuthType.EMAIL -> {
                vm.loginEmail(onLoading = onLoading(), onSuccess = {
                    if (it) {
                        callback?.onLoginSuccess(vm.loginType)
                    }
                }, onError = onErrorSimple())
            }
        }
    }

    override fun onLoginWithFacebookClicked() {
        callback?.getFacebookAccessToken({
            if (context != null && !isRemoving) {
                vm.loginWithFacebook("", onLoading(), {
                    callback?.onSuccessLoginWithFacebook()
                    dismiss()
                }, onErrorSimple())
            }
        },{

        })
    }

    override fun onForgotPasswordClicked() {
        callback?.showForgotPasswordScreen()
    }

    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }
}

