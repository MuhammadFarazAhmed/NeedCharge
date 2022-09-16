/*
 * Maqsab Driver
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package incubasys.needcharge.repositories.remote.client


object Stringtil {
    /**
     * Check if the given array contains the given value (with case-insensitive comparison).
     *
     * @param array The array
     * @param value The value to search
     * @return true if the array contains the value
     */
    fun containsIgnoreCase(array: Array<String>, value: String?): Boolean {
        for (str in array) {
            if (value == null && str == null) return true
            if (value != null && value.equals(str, ignoreCase = true)) return true
        }
        return false
    }

    /**
     * Join an array of strings with the given separator.
     *
     *
     * Note: This might be replaced by utility method from commons-lang or guava someday
     * if one of those libraries is added as dependency.
     *
     *
     * @param array     The array of strings
     * @param separator The separator
     * @return the resulting string
     */
    fun join(array: Array<String>, separator: String): String {
        val len = array.size
        if (len == 0) return ""

        val out = StringBuilder()
        out.append(array[0])
        for (i in 1 until len) {
            out.append(separator).append(array[i])
        }
        return out.toString()
    }
}
