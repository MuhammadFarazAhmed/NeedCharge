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
import incubasys.needcharge.authentication.callback.SignupCallback
import incubasys.needcharge.authentication.callback.SignupViewCallback
import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.authentication.databinding.SignupFragmentBinding
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.SignupViewModel
import incubasys.needcharge.base.getDrawable
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.utils.DrawableClickListener
import incubasys.needcharge.base.utils.Utils.ifLet
import javax.inject.Inject

class SignupFragment : AuthBaseFragment(), SignupViewCallback {
    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }

    companion object {
        private const val ARG = "signupType"
        fun newInstance(signupType: AuthType): SignupFragment {
            val f = SignupFragment()
            val bundle = Bundle()
            bundle.putInt(ARG, signupType.ordinal)
            f.arguments = bundle
            return f
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: SignupFragmentBinding
    private val vm: SignupViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
    }
    private var callback: SignupCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignupCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        arguments?.let {
            vm.signupType = AuthType.fromInt(it.getInt(ARG, 0))
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
                val mDrawable = getDrawable(incubasys.needcharge.base.R.drawable.ic_preview)
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
    }

    override fun onSignupClicked() {
        when (vm.signupType) {
            AuthType.PHONE -> {
                binding.etPhone.text?.let {
                    vm.generatePinForLoginWithPhone(onLoading = onLoading(), onSuccess = {
                        if (it)
                            ifLet(vm.fulName.value, vm.phone.value) { (name, phone) ->
                                callback?.showPhoneVerificationForSignup(name, phone)
                            }
                    }, onError = onErrorSimple())
                }
            }
            AuthType.EMAIL -> {
                vm.signupWithEmail(onLoading = onLoading(), onSuccess = {
                    if (it) {
                        callback?.signUpSucessful(vm.signupType)
                    }
                }, onError = onErrorSimple())

            }
        }

    }

    override fun onBackPressed() {
        dismiss()
    }
}
