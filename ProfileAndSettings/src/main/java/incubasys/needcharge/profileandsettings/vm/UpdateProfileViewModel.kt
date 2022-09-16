package incubasys.needcharge.profileandsettings.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldPassword
import com.mlykotom.valifi.fields.ValiFieldText
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.base.utils.Utils
import incubasys.needcharge.datainterfaces.models.EmailUpdate
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.NameUpdate
import incubasys.needcharge.datainterfaces.usecases.ProfileUsecase
import incubasys.needcharge.profileandsettings.R
import incubasys.needcharge.profileandsettings.data.UpdateType
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class UpdateProfileViewModel @Inject constructor(application: Application, private val profileUsecase: ProfileUsecase) : BaseViewModel(application) {

    lateinit var type: UpdateType
    var gotPassword = MutableLiveData<Boolean>(false)
    //  var gotPhone = MutableLiveData<Boolean>(false)
    var title = MutableLiveData<String>("")
    var description = MutableLiveData<String>("")
    val email = ValiFieldEmail()
    val phone = ValiFieldText()
    val password = ValiFieldPassword()
    val fulName = ValiFieldText()
    /*val code = ValiFieldInteger()*/
    val form = MutableLiveData<ValiFiForm>()

    fun revertEmailForum() {
        description.value = getString(R.string.please_provide_us_with_your_password_nin_order_to_change_your_email)
        gotPassword.value = false
        setForm()
    }

    fun updateEmailForum() {
        gotPassword.value = true
        description.value = getString(R.string.please_give_us_your_new_email_address)
        setForm()
    }

    fun setForm() {
        form.value = when (type) {
            UpdateType.EMAIL -> if (gotPassword.value!!) ValiFiForm(password, email) else ValiFiForm(password)
            UpdateType.PHONE -> ValiFiForm(phone) /*if (gotPhone.value!!) ValiFiForm(phone, code) else ValiFiForm(phone)*/
            UpdateType.NAME -> ValiFiForm(fulName)
        }
    }

    init {
        email.setEmptyAllowed(false)
        phone.setEmptyAllowed(false).addPatternValidator("Enter valid Phone", Pattern.compile("^\\+\\d{2} \\| \\d{3} \\d{4} \\d{3}\$"))
        password.setEmptyAllowed(false).addMinLengthValidator(8)
        fulName.addNotEmptyValidator().addPatternValidator("Enter valid Name", Pattern.compile("^[a-zA-Z]{3,}(?: [a-zA-Z]+){0,2}\$"))
        //  code.addNotEmptyValidator().addExactLengthValidator("Enter valid 4 digit code", 4)
    }

    fun updateEmail(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            Utils.ifLet(email.value, password.value) { (emailNonNull, passwordNonNull) ->
                profileUsecase.updateEmail(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = EmailUpdate(passwordNonNull, emailNonNull))
            }
        }
    }

    fun updateName(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            fulName.value?.let {
                profileUsecase.updateName(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = NameUpdate(it))
            }
        }
    }


    /*  fun updatePhone(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
          viewModelScope.launch {
              Utils.ifLet(phone.value, code.value) { (phoneNonNull, codeNonNull) ->
                  val simplePhone = phoneNonNull.replace("[+\\s|]".toRegex(), "")
                  profileUsecase.updatePhone(onLoading = onLoading,
                          onSuccess = onSuccess, onError = onError,
                          body = PhoneUpdate(phone = simplePhone.drop(3),
                                  countryCode = simplePhone.dropLast(9),
                                  verificationCode = codeNonNull))
              }
          }
      }*/

}
