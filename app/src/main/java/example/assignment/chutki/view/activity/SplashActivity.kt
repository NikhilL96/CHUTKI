package example.assignment.chutki.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.router.LoginRouter
import example.assignment.chutki.viewmodel.SplashActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: BaseActivity<SplashActivityViewModel>(false) {


    companion object {
        private const val DUMMY_DELAY = 2000L
    }

    override val layoutId: Int
        get() = R.layout.activity_splash
    override val viewModelType: Class<SplashActivityViewModel>
        get() = SplashActivityViewModel::class.java

    @Inject
    lateinit var router: LoginRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.fetchAllUser {
                if(it)
                    router.openLogin()
                 else
                    router.openRegister()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }, DUMMY_DELAY)

    }
}