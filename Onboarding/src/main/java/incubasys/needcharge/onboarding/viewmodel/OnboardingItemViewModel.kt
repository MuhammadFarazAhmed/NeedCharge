package incubasys.needcharge.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import incubasys.needcharge.onboarding.data.Onboarding

import javax.inject.Inject

class OnboardingItemViewModel @Inject
constructor() : ViewModel() {

    var onboarding: Onboarding? = null
}
