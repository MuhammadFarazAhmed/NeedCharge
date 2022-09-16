package incubasys.needcharge.authentication.data

enum class TermsType constructor(val value: Int) {
    TERMS(1), POLICY(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }
}