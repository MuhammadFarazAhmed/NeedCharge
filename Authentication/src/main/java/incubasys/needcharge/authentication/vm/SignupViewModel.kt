package incubasys.needcharge.authentication.vm

import android.app.Application
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldPassword
import com.mlykotom.valifi.fields.ValiFieldText
import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.base.utils.Utils
import incubasys.needcharge.datainterfaces.models.EmailSignupInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.SignupUsecase
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class SignupViewModel @Inject constructor(application: Application, private val signupUsecase: SignupUsecase) : BaseViewModel(application) {
    lateinit var signupType: AuthType
    val email = ValiFieldEmail()
    val phone = ValiFieldText()
    val password = ValiFieldPassword()
    val fulName = ValiFieldText()
    val form: ValiFiForm by lazy {
        when (signupType) {
            AuthType.EMAIL -> {
                ValiFiForm(fulName, email, password)
            }
            AuthType.PHONE -> {
                ValiFiForm(fulName, phone)
            }
        }
    }

    init {
        email.setEmptyAllowed(false)
        phone.setEmptyAllowed(false).addPatternValidator("Enter valid Phone", Pattern.compile("^\\+\\d{2} \\| \\d{3} \\d{4} \\d{3}\$"))
        password.setEmptyAllowed(false).addMinLengthValidator(8)
        fulName.addNotEmptyValidator().addPatternValidator("Enter valid Name", Pattern.compile("^[a-zA-Z]{3,}(?: [a-zA-Z]+){0,2}\$"))
    }


    fun signupWithEmail(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            Utils.ifLet(fulName.value, email.value, password.value) { (nameNonNull, emailNonNull, passwordNonNull) ->
                signupUsecase.signupWithEmail(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = EmailSignupInput(nameNonNull, emailNonNull, passwordNonNull))
            }
        }
    }

    fun generatePinForLoginWithPhone(onLoading : (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            Utils.ifLet(fulName.value, phone.value) { (phoneNonNull) ->
                val simplePhone = phoneNonNull.replace("[+\\s|]".toRegex(),"")
                signupUsecase.generatePin(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        phone = simplePhone.drop(3), countryCode = simplePhone.dropLast(9))
            }
        }
    }

}
