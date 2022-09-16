package incubasys.needcharge.authentication.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import incubasys.needcharge.authentication.data.TermsType
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.SupportContentOutput
import incubasys.needcharge.datainterfaces.usecases.AuthUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TermsViewModel @Inject constructor(application: Application, private val authUsecase: AuthUsecase) : BaseViewModel(application) {
    lateinit var type: TermsType
    var closeActivity: Boolean = false
    val content = MutableLiveData<String>()

    var contentAll: SupportContentOutput? = null

    fun getSupportContent(onLoading: (Boolean) -> Unit, onSuccess: (SupportContentOutput) -> Unit, onError: (Message) -> Unit) {
        if (contentAll == null) {
            viewModelScope.launch {
                authUsecase.getSupportContent(onLoading = onLoading,
                        onSuccess = {
                            contentAll = it
                            onSuccess.invoke(it)
                        }, onError = onError)
            }
        } else {
            contentAll?.let(onSuccess)
        }
    }
}
