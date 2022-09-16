package incubasys.needcharge.authentication.data

enum class VerificationType constructor(val value: Int) {
    SIGNUP(1), LOGIN(2), PHONE_UPDATE(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }
}