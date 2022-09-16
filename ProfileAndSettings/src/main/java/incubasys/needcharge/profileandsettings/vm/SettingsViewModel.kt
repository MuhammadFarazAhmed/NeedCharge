package incubasys.needcharge.profileandsettings.vm

import android.app.Application
import androidx.databinding.Observable
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.SettingsUsecase
import incubasys.needcharge.profileandsettings.data.Settings
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(application: Application, private val settingsUsecase: SettingsUsecase) : BaseViewModel(application) {
    val settingsList = mutableListOf<Settings>()

    /*private val isLoggedInCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

        }
    }*/

    init {
       // settingsUsecase.isLoggedIn().addOnPropertyChangedCallback(isLoggedInCallback)

        settingsList.clear()
        settingsList.add(Settings(id = 1, title = "OnBoarding"))
        if (isLoggedIn()) {
            settingsList.add(Settings(id = 2, title = "Change password"))
        }
        settingsList.add(Settings(id = 3, title = "Privacy"))
        settingsList.add(Settings(id = 4, title = "T&C"))
    }


    fun signout(onLoading: (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            settingsUsecase.signout(onLoading = onLoading,
                    onSuccess = onSuccess, onError = onError)
        }
    }

    fun isLoggedIn() = settingsUsecase.isLoggedIn().get()

   /* override fun onCleared() {
        settingsUsecase.isLoggedIn().removeOnPropertyChangedCallback(isLoggedInCallback)
        super.onCleared()
    }*/
}
