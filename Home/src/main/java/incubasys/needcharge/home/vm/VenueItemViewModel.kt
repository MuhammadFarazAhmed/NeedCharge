package incubasys.needcharge.home.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import incubasys.needcharge.datainterfaces.VenueItem

class VenueItemViewModel : ViewModel() {

     var venueItem = ObservableField<VenueItem>()

}