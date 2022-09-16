package incubasys.needcharge.onboarding.ui

import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.onboarding.callback.OnboardingCallback
import incubasys.needcharge.onboarding.databinding.OnboardingFragmentBinding
import incubasys.needcharge.onboarding.viewmodel.OnboardingViewModel
import javax.inject.Inject

class OnboardingFragment : BaseFragment(), OnboardingCallback {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var callback: OnboardingCallback? = null
    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProviders.of(this, factory).get(OnboardingViewModel::class.java)
    }
    private lateinit var binding: OnboardingFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnboardingCallback) {
            callback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardingFragmentBinding.inflate(inflater, container, false)
        binding.callback = this
        binding.lifecycleOwner = this
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setUpList()
        binding.vpPager.adapter = PagerAdapter(childFragmentManager)
        binding.cpiIndicator.setViewPager(binding.vpPager)
        binding.vpPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewModel.showFinishButton.value = (((viewModel.onboardingList.size - 1) == position))
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
            }

        })
    }

    override fun onFinishButtonClicked() {
        viewModel.onFinishButtonClicked()
        callback?.onFinishButtonClicked()
    }


    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    internal inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return OnboardingItemFragment.newInstance(viewModel.onboardingList[position])
        }

        override fun getCount(): Int {
            return viewModel.onboardingList.size
        }
    }
}
