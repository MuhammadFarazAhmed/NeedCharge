package incubasys.needcharge.authentication.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.authentication.R
import incubasys.needcharge.authentication.callback.AuthCallback
import incubasys.needcharge.authentication.callback.AuthViewCallback
import incubasys.needcharge.authentication.databinding.AuthFragmentBinding
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.AuthViewModel
import incubasys.needcharge.base.onLoading
import javax.inject.Inject


class AuthFragment : AuthBaseFragment(), AuthViewCallback {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: AuthFragmentBinding
    private val vm: AuthViewModel by lazy {
        ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
    }
    private var callback: AuthCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvPrivacyLabel.movementMethod = LinkMovementMethod.getInstance()
        val terms = "Terms"
        val policy = "Policy"
        val finalString = getString(R.string.by_signing_up_i_confirm_that_i_agree_with_n_terms_and_policy_uses)
        val spannableStringBuilder = SpannableStringBuilder(finalString)
        val startIndexTerms = finalString.indexOf(terms)
        val startIndexPolicy = finalString.indexOf(policy)
        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                vm.getSupportContent(onLoading(), {
                    it.terms?.let { terms -> callback?.showTermsScreen(terms) }
                }, onErrorSimple())
            }

        }, startIndexTerms, startIndexTerms + terms.length, 0)

        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                vm.getSupportContent(onLoading(), {
                    it.privacyPolicy?.let { policy -> callback?.showPrivacyPolicyScreen(policy) }
                }, onErrorSimple())

            }

        }, startIndexPolicy, startIndexPolicy + policy.length, 0)
        binding.tvPrivacyLabel.text = spannableStringBuilder

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback?.onBackPressed(tag)
    }


    override fun onLoginWithEmailClicked() {
        callback?.showEmailLoginScreen()
    }

    override fun onLoginWithPhoneClicked() {
        callback?.showPhoneLoginScreen()
    }

    override fun onLoginWithFacebookClicked() {
        callback?.getFacebookAccessToken({
            if (context != null && !isRemoving) {
                vm.loginWithFacebook("", onLoading(), {
                    callback?.onSuccessLoginWithFacebook()
                    dismiss()
                }, onErrorSimple())
            }
        }, {

        })


    }

    override fun onSignupWithEmailClicked() {
        callback?.showSignupWithEmailScreen()
    }

    override fun onSignupWithPhoneClicked() {
        callback?.showSignupWithPhoneScreen()
    }

    override fun onCloseButtonClicked() {
        dismiss()
    }

    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }

    companion object {
        fun newInstance(): AuthFragment {
            return AuthFragment()
        }
    }

}
