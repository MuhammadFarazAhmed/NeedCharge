package incubasys.needcharge.profileandsettings.data

enum class UpdateType constructor(val value: Int) {
    NAME(0), PHONE(1), EMAIL(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }
}