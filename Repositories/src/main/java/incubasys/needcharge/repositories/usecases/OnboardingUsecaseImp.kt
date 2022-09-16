package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.usecases.OnboardingUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import javax.inject.Inject

class OnboardingUsecaseImp @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    parseErrors: ParseErrors
) :
    BaseUsecase(parseErrors), OnboardingUsecase {
    override suspend fun setOnBoardingShown(isOnBoardingShown: Boolean) {
        preferencesHelper.isOnboardingShown = isOnBoardingShown
    }


}