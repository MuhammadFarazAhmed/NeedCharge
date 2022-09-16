package incubasys.needcharge.authentication.vm

import android.app.Application
import com.mlykotom.valifi.fields.number.ValiFieldInteger
import incubasys.needcharge.authentication.data.VerificationType
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.PhoneLoginInput
import incubasys.needcharge.datainterfaces.models.PhoneSignupInput
import incubasys.needcharge.datainterfaces.models.PhoneUpdate
import incubasys.needcharge.datainterfaces.usecases.VerificationUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class VerificationViewModel @Inject constructor(application: Application, private val verificationUsecase: VerificationUsecase) : BaseViewModel(application) {

    lateinit var verificationType: VerificationType
    val code = ValiFieldInteger()
    var phoneNumber: String = ""
    var fullName: String = ""
    var isVerified: Boolean = false

    init {
        code.addNotEmptyValidator().addExactLengthValidator("Enter valid 4 digit code", 4)
    }

    fun signup(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            code.value?.let {
                val simplePhone = phoneNumber.replace("[+\\s|]".toRegex(), "")
                PhoneSignupInput(fullName, simplePhone.drop(3), it, simplePhone.dropLast(9))
            }?.let {
                verificationUsecase.signUpWithPhone(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = it)
            }
        }
    }

    fun login(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            code.value?.let {
                val simplePhone = phoneNumber.replace("[+\\s|]".toRegex(), "")
                PhoneLoginInput(simplePhone.drop(3), it, simplePhone.dropLast(9))
            }?.let {
                verificationUsecase.loginWithPhone(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = it)
            }
        }
    }

    fun changePhone(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            code.value?.let {
                val simplePhone = phoneNumber.replace("[+\\s|]".toRegex(), "")
                PhoneUpdate(phone = simplePhone.drop(3), verificationCode = it, countryCode = simplePhone.dropLast(9) )
            }?.let {
                verificationUsecase.changePhone(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = it)
            }
        }
    }

    fun generatePinForLoginWithPhone(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            val simplePhone = phoneNumber.replace("[+\\s|]".toRegex(), "")
            verificationUsecase.generatePin(onLoading = onLoading,
                    onSuccess = onSuccess, onError = onError,
                    phone = simplePhone.drop(3), countryCode = simplePhone.dropLast(9))

        }
    }
}
