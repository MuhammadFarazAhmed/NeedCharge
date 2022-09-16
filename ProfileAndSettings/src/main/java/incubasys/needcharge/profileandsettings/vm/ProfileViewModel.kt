package incubasys.needcharge.profileandsettings.vm

import android.app.Application
import androidx.databinding.ObservableField
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.ProfileUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(application: Application, private val profileUsecase: ProfileUsecase) : BaseViewModel(application) {

    val email = ObservableField<String>("")
    val fullName = ObservableField<String>("")
    val phone = ObservableField<String>("")

    fun getUser(onLoading: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            profileUsecase.getUser(onLoading = onLoading,
                    onSuccess = {
                        email.set(it.email ?: "")
                        fullName.set(it.name)
                        phone.set(it.phone ?: "")
                    }, onError = onError)
        }
    }

}
