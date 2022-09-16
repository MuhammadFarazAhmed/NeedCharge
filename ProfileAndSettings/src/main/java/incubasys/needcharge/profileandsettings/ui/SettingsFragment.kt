package incubasys.needcharge.profileandsettings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.base.RecyclerViewCallback
import incubasys.needcharge.base.onLoading
import incubasys.needcharge.base.showErrorMessage
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.profileandsettings.adapter.SettingsAdapter
import incubasys.needcharge.profileandsettings.callback.SettingsCallback
import incubasys.needcharge.profileandsettings.callback.SettingsViewCallback
import incubasys.needcharge.profileandsettings.databinding.SettingsFragmentBinding
import incubasys.needcharge.profileandsettings.vm.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : BaseFragment(), RecyclerViewCallback, SettingsViewCallback {


    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: SettingsFragmentBinding
    private val vm: SettingsViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)
    }
    private var callback: SettingsCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSettingsList.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = SettingsAdapter(this)
        adapter.submitList(vm.settingsList)
        binding.rvSettingsList.adapter = adapter
    }

    override fun onBackPressed() {
        callback?.onBackPressed()
    }

    override fun onSignOutButtonClicked() {
        callback?.onSignOutClicked()
        vm.signout(onLoading(), {
            callback?.onSignOutSuccessful()
        }, onErrorSimple())
    }

    override fun onListItemClicked(position: Int) {
        when (vm.settingsList[position].id) {
            1 -> callback?.showOnBoardingScreen()
            2 -> callback?.showChangePasswordScreen()
            3 -> callback?.showPrivacyPolicyScreen()
            4 -> callback?.showTermsScreen()
        }
    }

    fun Fragment.onErrorSimple(): (Message) -> Unit {
        return {
            showErrorMessage(it.message)
        }
    }

}
