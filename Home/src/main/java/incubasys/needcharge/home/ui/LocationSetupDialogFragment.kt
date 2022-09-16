package incubasys.needcharge.home.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import incubasys.needcharge.home.R
import incubasys.needcharge.home.callback.PermissionDialogCallback
import incubasys.needcharge.home.vm.PermissionViewModel
import  incubasys.needcharge.home.databinding.LocationSetupDialogFragmentBinding

class LocationSetupDialogFragment : BottomSheetDialogFragment(), PermissionDialogCallback {
    private var messageType = 0

    private var mListener: Listener? = null
    private lateinit var binding: LocationSetupDialogFragmentBinding
    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            messageType = it.getInt(ARG_MESSAGE_TYPE, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LocationSetupDialogFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setMessageType(messageType)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog

    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is Listener) {
            mListener = parent
        } else if (context is Listener) {
            mListener = context
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    override fun onNegativeButtonClicked() {
        dismiss()
    }

    override fun onPositiveButtonClicked() {
        mListener?.onPermissionDialogButtonClicked(messageType)
        dismiss()
    }

    interface Listener {
        fun onPermissionDialogButtonClicked(messageType: Int)
    }

    companion object {

        const val TYPE_ALLOW_LOCATION_PERMISSION_DIALOG = 1
        const val TYPE_ALLOW_LOCATION_PERMISSION_SETTING = 2
        const val TYPE_LOCATION_SETTINGS_ON = 3

        private const val ARG_MESSAGE_TYPE = "messageType"

        fun newInstance(messageType: Int): LocationSetupDialogFragment {
            val fragment = LocationSetupDialogFragment()
            val args = Bundle()
            args.putInt(ARG_MESSAGE_TYPE, messageType)
            fragment.arguments = args
            return fragment
        }
    }
}
