package incubasys.needcharge.repositories

import incubasys.needcharge.datainterfaces.models.*
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.repositories.remote.api.AuthApi
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
        private val authApi: AuthApi
) : AuthRepository {

    override fun generatePin(phone: String, countryCode: String): Deferred<Response<Message>> = authApi.generatePin(phone, countryCode)
    override fun validateSession(): Deferred<Response<Session>> = authApi.validateSession()
    override fun verifyEmail(token: String): Deferred<Response<Message>> = authApi.verifyEmail(token)
    override fun loginWithPhone(body: PhoneLoginInput): Deferred<Response<Session>> = authApi.loginWithPhone(body)
    override fun loginWithEmail(body: EmailLoginInput): Deferred<Response<Session>> = authApi.loginWithEmail(body)
    override fun signUpWithPhone(body: PhoneSignupInput): Deferred<Response<Session>> = authApi.signUpWithPhone(body)
    override fun signUpWithEmail(body: EmailSignupInput): Deferred<Response<Session>> = authApi.signUpWithEmail(body)
    override fun signUpWithFacebook(body: FacebookLoginInput): Deferred<Response<Session>> = authApi.signUpWithFacebook(body)
    override fun forgotPassword(body: ForgotPasswordInput): Deferred<Response<Message>> = authApi.forgotPassword(body)
    /*When Logged In*/
    override fun changePassword(body: Password): Deferred<Response<Message>> = authApi.changePassword(body)

    /*When Not Logged In*/
    override fun resetPassword(body: Password): Deferred<Response<Message>> = authApi.resetPassword(body)

    override fun logout(): Deferred<Response<Message>> = authApi.logout()

    override fun getSupportContent(): Deferred<Response<SupportContentOutput>> = authApi.getSupportContent()

    override fun updatePhone(body: PhoneUpdate): Deferred<Response<User>> = authApi.updatePhone(body)
    override fun updateName(body: NameUpdate): Deferred<Response<User>>  = authApi.updateName(body)
    override fun updateEmail(body: EmailUpdate): Deferred<Response<User>>  = authApi.updateEmail(body)

}