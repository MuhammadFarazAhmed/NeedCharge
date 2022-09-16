package incubasys.needcharge.datainterfaces.repository

import incubasys.needcharge.datainterfaces.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface AuthRepository {
    fun generatePin(phone: String, countryCode: String): Deferred<Response<Message>>
    fun validateSession(): Deferred<Response<Session>>
    fun verifyEmail(token: String): Deferred<Response<Message>>
    fun loginWithPhone(body: PhoneLoginInput): Deferred<Response<Session>>
    fun loginWithEmail(body: EmailLoginInput): Deferred<Response<Session>>
    fun signUpWithPhone(body: PhoneSignupInput): Deferred<Response<Session>>
    fun signUpWithEmail(body: EmailSignupInput): Deferred<Response<Session>>
    fun signUpWithFacebook(body: FacebookLoginInput): Deferred<Response<Session>>
    fun forgotPassword(body: ForgotPasswordInput): Deferred<Response<Message>>
    /*When Logged In*/
    fun changePassword(body: Password): Deferred<Response<Message>>

    /*When Not Logged In*/
    fun resetPassword(body: Password): Deferred<Response<Message>>

    fun logout(): Deferred<Response<Message>>
    fun getSupportContent(): Deferred<Response<SupportContentOutput>>
    fun updatePhone(body: PhoneUpdate): Deferred<Response<User>>
    fun updateName(body: NameUpdate): Deferred<Response<User>>
    fun updateEmail(body: EmailUpdate): Deferred<Response<User>>
}