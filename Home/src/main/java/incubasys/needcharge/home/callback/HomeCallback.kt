package incubasys.needcharge.home.callback

import incubasys.needcharge.datainterfaces.VenueItem

interface HomeCallback {
    fun openMenu()
    fun showOnboardingScreen()
    fun showVenueDetail(venueItem: VenueItem)
    fun requestLocation()
}
