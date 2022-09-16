package incubasys.needcharge.authentication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import incubasys.needcharge.authentication.callback.ChangePasswordCallback
import incubasys.needcharge.authentication.callback.ChangePasswordViewCallback
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.ChangePasswordViewModel
import incubasys.needcharge.base.getDrawable
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.utils.DrawableClickListener
import javax.inject.Inject
import incubasys.needcharge.authentication.databinding.ChangePasswordFragmentBinding

class ChangePasswordFragment : AuthBaseFragment(), ChangePasswordViewCallback {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ChangePasswordFragmentBinding
    private val vm: ChangePasswordViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ChangePasswordViewModel::class.java)
    }
    private var callback: ChangePasswordCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChangePasswordCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.etNewPassword.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etNewPassword) {
            override fun onDrawableClick(): Boolean {
                val mDrawable = getDrawable(incubasys.needcharge.base.R.drawable.ic_preview)
                if (binding.etNewPassword.transformationMethod == null) {
                    binding.etNewPassword.transformationMethod = PasswordTransformationMethod()
                    mDrawable?.colorFilter = null
                } else {
                    binding.etNewPassword.transformationMethod = null
                    mDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor("#D4DCE1"), PorterDuff.Mode.MULTIPLY)
                }
                mDrawable?.let {
                    binding.etNewPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(binding.etNewPassword.compoundDrawablesRelative[0], null, mDrawable, null)
                }
                return false
            }
        })
        binding.etNewPassword.transformationMethod = PasswordTransformationMethod()

        binding.etConfirmPassword.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etConfirmPassword) {
            override fun onDrawableClick(): Boolean {
                val mDrawable = getDrawable(incubasys.needcharge.base.R.drawable.ic_preview)
                if (binding.etConfirmPassword.transformationMethod == null) {
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod()
                    mDrawable?.colorFilter = null
                } else {
                    binding.etConfirmPassword.transformationMethod = null
                    mDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor("#D4DCE1"), PorterDuff.Mode.MULTIPLY)
                }
                mDrawable?.let {
                    binding.etConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(binding.etConfirmPassword.compoundDrawablesRelative[0], null, mDrawable, null)
                }
                return false
            }
        })
        binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod()

    }


    override fun onCloseButtonClicked() {
        dismiss()
    }

    override fun onChangePasswordButtonClicked() {
        vm.changePassword(onLoading(), {
            callback?.onPasswordChangedSuccessfuly()
        }, onErrorSimple())
    }

    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }

    override fun onDismiss(dialog: DialogInterface) {
        callback?.onBackPressed(tag)
        super.onDismiss(dialog)
    }
}
