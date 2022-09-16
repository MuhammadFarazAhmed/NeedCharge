package incubasys.needcharge.authentication.data

enum class AuthType constructor(val value: Int) {
    PHONE(1), EMAIL(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }
}