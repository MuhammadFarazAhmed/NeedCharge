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
 * @param deviceToken
 * @param deviceType 0 => Android 1 => iOS
 */
data class DeviceInput(
        val deviceToken: String,
        /* 0 => Android 1 => iOS */
        val deviceType: Int

) {
}