package incubasys.needcharge.profileandsettings.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.utils.DrawableClickListener
import incubasys.needcharge.profileandsettings.callback.ProfileCallback
import incubasys.needcharge.profileandsettings.callback.ProfileViewCallback
import incubasys.needcharge.profileandsettings.databinding.ProfileFragmentBinding
import incubasys.needcharge.profileandsettings.vm.ProfileViewModel
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileViewCallback {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ProfileFragmentBinding
    private val vm: ProfileViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
    }
    private var callback: ProfileCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.getUser(onLoading(), {})
    }

    override fun onBackPressed() {
        callback?.onBackPressed()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.etFullName.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etFullName) {
            override fun onDrawableClick(): Boolean {
                callback?.showUpdateNameScreen()
                return false
            }
        })

        binding.etEmail.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etEmail) {
            override fun onDrawableClick(): Boolean {
                callback?.showUpdateEmailScreen()
                return false
            }
        })

        binding.etPhone.setOnTouchListener(object :
                DrawableClickListener.RightDrawableClickListener(binding.etPhone) {
            override fun onDrawableClick(): Boolean {
                callback?.showUpdatePhoneScreen()
                return false
            }
        })
    }
}