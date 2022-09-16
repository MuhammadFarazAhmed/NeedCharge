package incubasys.needcharge.authentication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import incubasys.needcharge.authentication.R
import incubasys.needcharge.authentication.callback.ForgotCallback
import incubasys.needcharge.authentication.callback.ForgotViewCallback
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.ForgotPasswordViewModel
import incubasys.needcharge.base.showErrorMessage
import javax.inject.Inject
import incubasys.needcharge.authentication.databinding.ForgotPasswordFragmentBinding
import incubasys.needcharge.base.closeKeyboard
import incubasys.needcharge.base.hideProgress
import incubasys.needcharge.base.showProgress


class ForgotPasswordFragment : AuthBaseFragment(), ForgotViewCallback {


    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ForgotPasswordFragmentBinding
    private val vm: ForgotPasswordViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ForgotPasswordViewModel::class.java)
    }
    private var callback: ForgotCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ForgotCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = ForgotPasswordFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onCloseButtonClicked() {
        dismiss()
    }

    override fun onSendButtonClicked() {
        binding.bSend.tag?.takeIf {
            it == "finish"
        }?.apply {
            callback?.onForgotEmailSentSuccessfuly()
        }
        vm.getEmailSent().observe(this, Observer {
            if (it) {
                binding.etEmail.visibility = View.GONE
                binding.bSend.text = getString(R.string.continue_)
                binding.bSend.tag = "finish"
                binding.tvSubText.text = getString(R.string.thanks_email_sent_to_reset_password)
                binding.etEmail.closeKeyboard()
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
            }
        })
        vm.getError().observe(this, Observer {
            showErrorMessage(it.message)
        })
        vm.getShowProgress().observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })
        vm.sendEmail()
    }


    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }
}
