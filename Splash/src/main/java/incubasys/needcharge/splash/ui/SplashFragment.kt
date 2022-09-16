package incubasys.needcharge.splash.ui

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import incubasys.needcharge.base.BaseFragment
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.splash.callback.SplashCallback
import incubasys.needcharge.splash.databinding.SplashFragmentBinding
import incubasys.needcharge.splash.vm.SplashViewModel
import javax.inject.Inject

class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var binding: SplashFragmentBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }
    private var callback: SplashCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SplashCallback) {
            callback = context
        } else throw RuntimeException("SplashCallback not implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateLogo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getResult().observe(this, Observer {
            if (it.showOnBoarding)
                callback?.onStartActivity(Navigator.Modules.ONBOARDING)
            else
                callback?.onStartActivity(Navigator.Modules.HOME)
        })
        viewModel.getError().observe(this, Observer {
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
            callback?.onStartActivity(Navigator.Modules.ONBOARDING)
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        })
        viewModel.getShowProgress().observe(this, Observer {

        })
        viewModel.checkLogin()
    }

    private fun animateLogo() {
        val d = binding.ivImage.drawable
        if (d is AnimatedVectorDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                d.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        binding.ivImage.post {
                            d.start()
                        }
                    }
                })
            }
            d.start()
        } else if (d is AnimatedVectorDrawableCompat) {
            d.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    binding.ivImage.post {
                        d.start()
                    }
                }
            })
            d.start()
        }
    }

}
