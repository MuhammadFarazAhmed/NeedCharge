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

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 *
 * @param id
 * @param name
 * @param email
 * @param phone
 * @param emailVerified
 * @param countryCode
 * @param isUser
 * @param isAdmin
 */
@Parcelize
data class User(
        val id: Int,
        val name: String,
        val emailVerified: Boolean,
        val countryCode: String,
        val isUser: Boolean,
        val isAdmin: Boolean
        ,
        val email: String? = null,
        val phone: String? = null
) : Parcelable