package incubasys.needcharge.home.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import incubasys.needcharge.home.R
import incubasys.needcharge.home.callback.BusinessDetailCallback
import incubasys.needcharge.home.callback.BusinessDetailViewCallback
import incubasys.needcharge.home.databinding.BusinessDetailFragmentBinding
import incubasys.needcharge.home.utils.HomeBaseFragment
import incubasys.needcharge.home.vm.HomeViewModel

class BusinessDetailFragment : HomeBaseFragment(), BusinessDetailViewCallback, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onPhoneIconClicked() {
        callback?.dialPhoneNumber()
    }

    override fun onChargeHereIconClicked() {
        callback?.openOtherModule()
    }

    companion object {
        fun newInstance() = BusinessDetailFragment()
    }

    private lateinit var binding: BusinessDetailFragmentBinding

    private val vm: HomeViewModel by lazy {
        (activity as HomeActivity).getHomeViewModel()
    }

    private var callback: BusinessDetailCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BusinessDetailCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = BusinessDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val mapFragment = childFragmentManager
                .findFragmentById(R.id.static_map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

}
