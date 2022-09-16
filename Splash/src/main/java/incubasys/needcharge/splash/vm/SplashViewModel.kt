package incubasys.needcharge.splash.vm

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.SplashUsecase
import incubasys.needcharge.splash.data.SplashInfo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val splashUsecase: SplashUsecase, application: Application) :
    BaseViewModel(application) {
    private val result = MutableLiveData<SplashInfo>()
    private val error = MutableLiveData<Message>()
    private val showProgress = MutableLiveData<Boolean>()
     val showRetryButton = ObservableBoolean(false)

    fun checkLogin() {
        viewModelScope.launch {
            showRetryButton.set(false)
            splashUsecase.validateSession(
                onLoading = {
                  onLoading()
                }, onSuccess = {
                    result.value = SplashInfo(true, splashUsecase.showOnboardingScreen())
                }, onError = {
                    if (it.code == 403) {
                        result.value = SplashInfo(false, splashUsecase.showOnboardingScreen())
                    } else {
                        showRetryButton.set(true)
                        error.value = it
                    }
                })
        }
    }


    fun getResult(): LiveData<SplashInfo> = result
    fun getError(): LiveData<Message> = error

}
