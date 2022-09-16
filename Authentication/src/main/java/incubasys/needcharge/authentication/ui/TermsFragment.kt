package incubasys.needcharge.authentication.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.authentication.callback.TermsViewCallback
import incubasys.needcharge.authentication.data.TermsType
import incubasys.needcharge.authentication.databinding.TermsFragmentBinding
import incubasys.needcharge.authentication.utils.AuthBaseFragment
import incubasys.needcharge.authentication.vm.TermsViewModel
import incubasys.needcharge.base.FragmentCallback
import incubasys.needcharge.base.onLoading
import javax.inject.Inject

class TermsFragment : AuthBaseFragment(), TermsViewCallback {

    companion object {

        private const val ARG = "termsType"
        private const val ARG_CONTENT = "termsContent"
        private const val ARG_CLOSE = "close"
        fun newInstance(termsType: TermsType, content: String = "", closeActivity: Boolean = false): TermsFragment {
            val f = TermsFragment()
            val bundle = Bundle()
            bundle.putInt(ARG, termsType.ordinal)
            bundle.putString(ARG_CONTENT, content)
            bundle.putBoolean(ARG_CLOSE, closeActivity)
            f.arguments = bundle
            return f
        }
    }

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: TermsFragmentBinding

    private val vm: TermsViewModel by lazy {
        ViewModelProviders.of(this, factory).get(TermsViewModel::class.java)
    }
    private var callback: FragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCallback) {
            callback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = TermsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.callback = this
        arguments?.let {
            vm.type = TermsType.fromInt(it.getInt(ARG, 0))
            vm.content.value = it.getString(ARG_CONTENT, "")
            vm.closeActivity = it.getBoolean(ARG_CLOSE, false)
        }
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTermsText.movementMethod = ScrollingMovementMethod()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (vm.content.value.isNullOrEmpty())
            vm.getSupportContent(onLoading(), {
                when (vm.type) {
                    TermsType.TERMS -> vm.content.value = it.terms
                    TermsType.POLICY -> vm.content.value = it.privacyPolicy
                }
            }, {
                onCloseButtonClicked()
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            })
    }


    override fun onCloseButtonClicked() {
        dismiss()
    }

    override fun onProgressDialogCancelled() {
        vm.cancelRequest()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (vm.closeActivity) {
            callback?.onBackPressed(tag)
        }
    }

}
