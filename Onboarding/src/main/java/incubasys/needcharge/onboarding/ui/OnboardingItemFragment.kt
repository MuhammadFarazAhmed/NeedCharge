package incubasys.needcharge.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import incubasys.needcharge.onboarding.data.Onboarding
import incubasys.needcharge.onboarding.databinding.OnboardingItemFragmentBinding
import incubasys.needcharge.onboarding.viewmodel.OnboardingItemViewModel


class OnboardingItemFragment : Fragment() {

    private lateinit var binding: OnboardingItemFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardingItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(OnboardingItemViewModel::class.java)
        viewModel.onboarding = arguments?.getParcelable(ARG_ONBOARDING)
        binding.vm = viewModel
    }

    companion object {
        private const val ARG_ONBOARDING = "onBoarding"
        fun newInstance(onboarding: Onboarding): OnboardingItemFragment {
            val fragment = OnboardingItemFragment()
            val args = Bundle()
            args.putParcelable(ARG_ONBOARDING, onboarding)
            fragment.arguments = args
            return fragment
        }
    }
}
