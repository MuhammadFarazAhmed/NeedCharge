package incubasys.needcharge.repositories.remote.api

import incubasys.needcharge.datainterfaces.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @GET("auth/generate_pin")
    fun generatePin(@Query("phone") phone: String, @Query("country_code") countryCode: String): Deferred<Response<Message>>

    @GET("auth/validate_session")
    fun validateSession(): Deferred<Response<Session>>

    @GET("auth/verify_email")
    fun verifyEmail(@Query("token") token: String): Deferred<Response<Message>>

    @POST("auth/login_phone")
    fun loginWithPhone(@Body body: PhoneLoginInput): Deferred<Response<Session>>

    @POST("auth/email_login")
    fun loginWithEmail(@Body body: EmailLoginInput): Deferred<Response<Session>>

    @POST("auth/phone_signup")
    fun signUpWithPhone(@Body body: PhoneSignupInput): Deferred<Response<Session>>

    @POST("auth/email_signup")
    fun signUpWithEmail(@Body body: EmailSignupInput): Deferred<Response<Session>>

    @POST("auth/facebook_login")
    fun signUpWithFacebook(@Body body: FacebookLoginInput): Deferred<Response<Session>>

    @POST("auth/forgot_password")
    fun forgotPassword(@Body body: ForgotPasswordInput): Deferred<Response<Message>>

    /*When Logged In*/
    @PUT("auth/change_password")
    fun changePassword(@Body body: Password): Deferred<Response<Message>>

    @PUT("users/update_phone")
    fun updatePhone(@Body body: PhoneUpdate): Deferred<Response<User>>

    @PUT("users/update_name")
    fun updateName(@Body body: NameUpdate): Deferred<Response<User>>

    @PUT("users/update_email")
    fun updateEmail(@Body body: EmailUpdate): Deferred<Response<User>>

    /*When Not Logged In*/
    @PUT("auth/reset_password")
    fun resetPassword(@Body body: Password): Deferred<Response<Message>>

    @GET("auth/logout")
    fun logout(): Deferred<Response<Message>>

    @GET("support_contents")
    fun getSupportContent(): Deferred<Response<SupportContentOutput>>
}