package incubasys.needcharge.datainterfaces.usecases

interface OnboardingUsecase {
    suspend fun setOnBoardingShown(
        isOnBoardingShown: Boolean
    )
}