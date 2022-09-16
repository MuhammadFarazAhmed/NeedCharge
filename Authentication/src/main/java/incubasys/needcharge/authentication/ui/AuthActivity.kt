package incubasys.needcharge.authentication.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.mlykotom.valifi.ValiFi
import incubasys.needcharge.authentication.R
import incubasys.needcharge.authentication.callback.*
import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.authentication.data.TermsType
import incubasys.needcharge.authentication.data.VerificationType
import incubasys.needcharge.base.BaseActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.base.showErrorMessage
import kotlinx.android.synthetic.main.auth_activity.*
import java.util.*


class AuthActivity : BaseActivity(), AuthCallback, LoginCallback, SignupCallback, VerificationCallback, ForgotCallback, ChangePasswordCallback {

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }
    var closeActivity = true

    private val goto: Int by lazy {
        if (intent.getBundleExtra(Navigator.EXTRAS) != null)
            intent.getBundleExtra(Navigator.EXTRAS).getInt(Navigator.AUTH_ARG_GOTO, -1)
        else
            -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Utils.getFacebookKeyHash(applicationContext)
        ValiFi.install(applicationContext)
        setContentView(R.layout.auth_activity)

        when (goto) {
            0 -> TermsFragment.newInstance(TermsType.TERMS, "", true).show(supportFragmentManager.beginTransaction(), "TermsFragment")
            1 -> TermsFragment.newInstance(TermsType.POLICY, "", true).show(supportFragmentManager.beginTransaction(), "TermsFragment")
            2 -> {
                val phone = if (intent.getBundleExtra(Navigator.EXTRAS) != null)
                    intent.getBundleExtra(Navigator.EXTRAS).getString(Navigator.AUTH_ARG_PHONE, null)
                else
                    null
                if (phone != null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fAuthContainer,
                            VerificationFragment.newInstance(VerificationType.PHONE_UPDATE, phone), "VerificationFragment").commit()
                } else {
                    finish()
                }
            }
            3 -> ChangePasswordFragment.newInstance().show(supportFragmentManager.beginTransaction(), "ChangePasswordFragment")
            -1 -> AuthFragment.newInstance().show(supportFragmentManager.beginTransaction(), "AuthFragment")
        }


        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        fbSuccessCallback.invoke(loginResult.accessToken.token)
                        fbSuccessCallback = {}
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        exception.message?.let {
                            fbErrorCallback.invoke(it)
                            fbErrorCallback = {}
                            showErrorMessage(it, fAuthContainer)
                        }
                    }
                })
    }

    override fun onBackPressed(tag: String?) {
        if (closeActivity) {
            finish()
        }
    }

    override fun showPhoneLoginScreen() {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("LoginFragment")
        LoginFragment.newInstance(AuthType.PHONE).show(ft, "LoginFragment")
    }

    override fun showEmailLoginScreen() {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("LoginFragment")
        LoginFragment.newInstance(AuthType.EMAIL).show(ft, "LoginFragment")
    }

    override fun onSuccessLoginWithFacebook() {

    }

    override fun showSignupWithEmailScreen() {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("SignupFragment")
        SignupFragment.newInstance(AuthType.EMAIL).show(ft, "SignupFragment")
    }

    override fun showSignupWithPhoneScreen() {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("SignupFragment")
        SignupFragment.newInstance(AuthType.PHONE).show(ft, "SignupFragment")
    }

    override fun showPrivacyPolicyScreen(content: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("TermsFragment")
        TermsFragment.newInstance(TermsType.POLICY, content, false).show(ft, "TermsFragment")
    }

    override fun showTermsScreen(content: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("TermsFragment")
        TermsFragment.newInstance(TermsType.TERMS, content, false).show(ft, "TermsFragment")
    }

    override fun onLoginSuccess(authType: AuthType) {
        closeActivity = true
        closeAllDialogs()
        finish()
    }

    override fun showForgotPasswordScreen() {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("ForgotPasswordFragment")
        ForgotPasswordFragment.newInstance().show(ft, "ForgotPasswordFragment")
    }

    override fun signUpSucessful(signupType: AuthType) {
        closeActivity = true
        closeAllDialogs()
        finish()
    }

    private fun closeAllDialogs() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.findFragmentByTag(it.tag)?.let { it1 ->
                (it1 as DialogFragment).dismiss()
            }
        }
    }

    override fun showPhoneVerificationForLogin(phone: String) {
        closeActivity = false
        closeAllDialogs()
        supportFragmentManager.beginTransaction().replace(R.id.fAuthContainer,
                VerificationFragment.newInstance(VerificationType.LOGIN, phone), "VerificationFragment").commit()
    }


    override fun showPhoneVerificationForSignup(fullName: String, phone: String) {
        closeActivity = false
        closeAllDialogs()
        supportFragmentManager.beginTransaction().replace(R.id.fAuthContainer,
                VerificationFragment.newInstance(VerificationType.SIGNUP, fullName, phone), "VerificationFragment").commit()
    }


    override fun onVerificationSuccessful(verificationType: VerificationType) {
        when (verificationType) {
            VerificationType.SIGNUP -> signUpSucessful(AuthType.PHONE)
            VerificationType.LOGIN -> onLoginSuccess(AuthType.PHONE)
            VerificationType.PHONE_UPDATE -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onForgotEmailSentSuccessfuly() {
        onBackPressed()
    }

    override fun onProgressDialogCancelled() {

    }

    private var fbSuccessCallback: (String) -> Unit? = {}
    private var fbErrorCallback: (String) -> Unit? = {}

    override fun getFacebookAccessToken(onGotToken: (String) -> Unit, onError: (String) -> Unit) {
        this.fbSuccessCallback = onGotToken
        this.fbErrorCallback = onError
        LoginManager.getInstance().logIn(this, Arrays.asList("public_profile", "email"))
    }

    override fun onPasswordChangedSuccessfuly() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
        super.onDestroy()
    }
}
