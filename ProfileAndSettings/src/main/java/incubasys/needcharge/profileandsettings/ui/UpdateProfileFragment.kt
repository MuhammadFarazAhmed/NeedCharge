package incubasys.needcharge.profileandsettings.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.base.getDrawable
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.showErrorMessage
import incubasys.needcharge.base.utils.DrawableClickListener
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.profileandsettings.R
import incubasys.needcharge.profileandsettings.callback.ProfileViewCallback
import incubasys.needcharge.profileandsettings.callback.UpdateProfileCallback
import incubasys.needcharge.profileandsettings.callback.UpdateProfileViewCallback
import incubasys.needcharge.profileandsettings.data.UpdateType
import incubasys.needcharge.profileandsettings.databinding.UpdateProfileFragmentBinding
import incubasys.needcharge.profileandsettings.vm.UpdateProfileViewModel
import javax.inject.Inject

class UpdateProfileFragment : BaseFragment(), ProfileViewCallback, UpdateProfileViewCallback {

    companion object {
        const val ARG_TYPE = "TYPE"
        fun newInstance(type: UpdateType): UpdateProfileFragment {
            val f = UpdateProfileFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TYPE, type.value)
            f.arguments = bundle
            return f
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: UpdateProfileFragmentBinding
    private val vm: UpdateProfileViewModel by lazy {
        ViewModelProviders.of(this, factory).get(UpdateProfileViewModel::class.java)
    }
    private var callback: UpdateProfileCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UpdateProfileCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = UpdateProfileFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            vm.type = UpdateType.fromInt(it.getInt(ARG_TYPE, 1))
        }
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
        binding.etPassword.transformationMethod = PasswordTransformationMethod()

        when (vm.type) {
            UpdateType.NAME -> {
                vm.title.value = getString(R.string.update_name)
                vm.description.value = getString(R.string.please_give_us_your_name)
            }
            UpdateType.PHONE -> {
                vm.title.value = getString(R.string.new_phone_number)
                vm.description.value = getString(R.string.please_give_us_your_mobile_number)
            }
            UpdateType.EMAIL -> {
                vm.title.value = getString(R.string.update_email)
                vm.description.value = getString(R.string.please_provide_us_with_your_password_nin_order_to_change_your_email)
            }
        }
        vm.setForm()
    }

    override fun onBackPressed() {
        vm.gotPassword.value?.let {
            if (it) {
                vm.revertEmailForum()
            } else {
                callback?.onBackPressed()
            }
        }
    }


    override fun onNextButtonClicked() {
        when (vm.type) {
            UpdateType.NAME -> {
                vm.updateName(onLoading(), {
                    if (it) {
                        callback?.onUpdateSuccessful(vm.type)
                    }
                }, onErrorSimple())
            }
            UpdateType.PHONE -> vm.phone.value?.let { callback?.onUpdatePhoneClicked(it) }
            UpdateType.EMAIL -> {
                vm.gotPassword.value?.let { gotPassword ->
                    if (gotPassword) {
                        vm.updateEmail(onLoading(), {
                            if (it) {
                                callback?.onUpdateSuccessful(vm.type)
                            }
                        }, onErrorSimple())
                    } else {
                        vm.updateEmailForum()
                        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
                    }
                }
            }
        }
    }

    override fun onResendButtonClicked() {

    }

    fun Fragment.onErrorSimple(): (Message) -> Unit {
        return {
            showErrorMessage(it.message)
        }
    }
}