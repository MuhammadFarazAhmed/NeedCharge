package incubasys.needcharge.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var job = Job()
    protected var viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var showProgress = MutableLiveData<Boolean>(false)

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    fun getString(@StringRes idRes: Int): String =
            getApplication<Application>().getString(idRes)

    fun cancelRequest() {
        viewModelScope.cancel()
    }

    fun getShowProgress(): LiveData<Boolean> = showProgress

    protected fun onLoading(): (Boolean) -> Unit {
        return {
            showProgress.value = it
        }
    }

}