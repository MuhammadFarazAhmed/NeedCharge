/**
 * NeedCharge Mobile
 * Mobile API for needcharge
 *
 *
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package incubasys.needcharge.datainterfaces.models


/**
 *
 * @param name
 * @param phone
 * @param pin
 * @param countryCode
 */
data class PhoneSignupInput(
        val name: String,
        val phone: String,
        val pin: String,
        val countryCode: String

)