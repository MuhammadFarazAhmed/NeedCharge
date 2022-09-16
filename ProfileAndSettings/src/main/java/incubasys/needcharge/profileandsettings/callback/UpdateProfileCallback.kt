package incubasys.needcharge.profileandsettings.callback

import incubasys.needcharge.profileandsettings.data.UpdateType

interface UpdateProfileCallback {

    fun onBackPressed()
    fun onUpdatePhoneClicked(phone: String)
    fun onUpdateSuccessful(type: UpdateType)
}
