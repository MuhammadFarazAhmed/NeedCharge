package incubasys.needcharge.onboarding.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.usecases.OnboardingUsecase
import incubasys.needcharge.onboarding.R
import incubasys.needcharge.onboarding.data.Onboarding
import javax.inject.Inject

class OnboardingViewModel @Inject
constructor(application: Application, private val onboardingUsecase: OnboardingUsecase) : BaseViewModel(application) {

    val onboardingList: MutableList<Onboarding> = ArrayList()

    val showFinishButton = MutableLiveData<Boolean>(false)

    fun setUpList() {
        onboardingList.add(
            Onboarding(
                1,
                R.drawable.bg_1,
                R.drawable.onboarding_1,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_description_1)
            )
        )
        onboardingList.add(
            Onboarding(
                2,
                R.drawable.bg_1,
                R.drawable.onboarding_2,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_description_2)
            )
        )
        onboardingList.add(
            Onboarding(
                3,
                R.drawable.bg_2,
                R.drawable.onboarding_3,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_description_3)
            )
        )
        onboardingList.add(
            Onboarding(
                4,
                R.drawable.bg_3,
                R.drawable.onboarding_4,
                getString(R.string.onboarding_title_4),
                getString(R.string.onboarding_description_4)
            )
        )
        onboardingList.add(
            Onboarding(
                5,
                R.drawable.bg_4,
                R.drawable.onboarding_5,
                getString(R.string.onboarding_title_5),
                getString(R.string.onboarding_description_5)
            )
        )

    }

    fun onFinishButtonClicked() {
        /*viewModelScope.launch {
            onboardingUsecase.setOnBoardingShown(true)
        }*/
    }
}
