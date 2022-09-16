package incubasys.needcharge.authentication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import incubasys.needcharge.authentication.R
import incubasys.needcharge.authentication.callback.VerificationCallback
import incubasys.needcharge.authentication.callback.VerificationViewCallback
import incubasys.needcharge.authentication.data.VerificationType
import incubasys.needcharge.authentication.databinding.VerificationFragmentBinding
import incubasys.needcharge.authentication.vm.VerificationViewModel
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.base.closeKeyboard
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.showErrorMessage
import incubasys.needcharge.base.utils.Utils
import incubasys.needcharge.datainterfaces.models.Message
import javax.inject.Inject

class VerificationFragment : BaseFragment(), VerificationViewCallback {

    companion object {
        private const val ARG = "VerificationType"
        private const val ARG_PHONE = "phone"
        private const val ARG_NAME = "name"
        fun newInstance(verificationType: VerificationType, phone: String? = null, fullName: String? = null): VerificationFragment {
            val f = VerificationFragment()
            val bundle = Bundle()
            bundle.putInt(ARG, verificationType.ordinal)
            bundle.putString(ARG_PHONE, phone)
            bundle.putString(ARG_NAME, fullName)
            f.arguments = bundle
            return f
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: VerificationFragmentBinding
    private val vm: VerificationViewModel by lazy {
        ViewModelProviders.of(this, factory).get(VerificationViewModel::class.java)
    }
    private var callback: VerificationCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is VerificationCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = VerificationFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.vm = vm
        arguments?.let {
            vm.verificationType = VerificationType.fromInt(it.getInt(ARG, 0))
            vm.phoneNumber = it.getString(ARG_PHONE, "")
            vm.fullName = it.getString(ARG_NAME, "")
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (vm.verificationType == VerificationType.PHONE_UPDATE) {
            binding.ivBack.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = 0
            }
            onResendButtonClicked()
        }
    }

    override fun onVerifyButtonClicked() {
        binding.bNext.tag?.takeIf {
            it == "finish"
        }?.apply {
            callback?.onVerificationSuccessful(vm.verificationType)
            return
        }
        when (vm.verificationType) {
            VerificationType.SIGNUP -> vm.signup(onLoading(), {
                if (it) {
                    vm.isVerified = true
                    binding.bResend.visibility = View.GONE
                    binding.etCode.visibility = View.GONE
                    binding.bNext.text = getString(R.string.continue_)
                    binding.bNext.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        marginStart = Utils.convertDpToPixel(25f, requireContext()).toInt()
                        marginEnd = Utils.convertDpToPixel(25f, requireContext()).toInt()
                    }
                    binding.bNext.tag = "finish"
                    binding.tvSubText.text = getString(R.string.phone_number_has_been_verified)
                    binding.etCode.closeKeyboard()
                    TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
                }
            }, onErrorSimple())
            VerificationType.LOGIN -> vm.login(onLoading(), {
                if (it) {
                    vm.isVerified = true
                    callback?.onVerificationSuccessful(VerificationType.LOGIN)
                }
            }, onErrorSimple())
            VerificationType.PHONE_UPDATE -> vm.changePhone(onLoading(), {
                if (it) {
                    vm.isVerified = true
                    callback?.onVerificationSuccessful(VerificationType.PHONE_UPDATE)
                }
            }, onErrorSimple())
        }
    }

    override fun onResendButtonClicked() {
        vm.generatePinForLoginWithPhone(onLoading(), {
            Toast.makeText(activity, "Pin Sent!", Toast.LENGTH_SHORT).show()
        }, onErrorSimple())
    }

    override fun onBackPressed() {
        if (vm.isVerified) {
            context?.let {
                val dialog = AlertDialog.Builder(it)
                dialog.setTitle("Close")
                dialog.setMessage("Please verify your phone number then close the window.")
                dialog.setCancelable(true)
                dialog.create().show()
            }
        }
        callback?.onBackPressed(tag)
    }

    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }

    fun Fragment.onErrorSimple(): (Message) -> Unit {
        return {
            showErrorMessage(it.message)
        }
    }
}
