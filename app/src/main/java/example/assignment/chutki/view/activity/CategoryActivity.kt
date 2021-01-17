package example.assignment.chutki.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.UserDetailsManager
import example.assignment.chutki.router.CategoryActivityRouter
import example.assignment.chutki.router.LoginRouter
import example.assignment.chutki.view.adapter.CategoriesAdapter
import example.assignment.chutki.view.adapter.CategoryItemCallback
import example.assignment.chutki.viewmodel.CategoryActivityViewModel
import kotlinx.android.synthetic.main.activity_categories.*
import javax.inject.Inject

@AndroidEntryPoint
class CategoryActivity : BaseActivity<CategoryActivityViewModel>(true), CategoryItemCallback {

    override val layoutId: Int
        get() = R.layout.activity_categories
    override val viewModelType: Class<CategoryActivityViewModel>
        get() = CategoryActivityViewModel::class.java

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerAdapter: CategoriesAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var logoutButton: MaterialButton
    private lateinit var drawerLayoutEmailTv: TextView
    private lateinit var hamburgerIcon: ImageView

    @Inject lateinit var router: CategoryActivityRouter
    @Inject lateinit var loginRouter: LoginRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        viewModel.getCategories(this::onSuccess, this::onFailure)
    }

    private fun initUI() {
        categoriesRecyclerView = categories_recycler_view
        drawerLayout = base_drawer_layout
        drawerLayoutEmailTv = drawer_layout_email_tv
        logoutButton = logout_button
        hamburgerIcon = hamburger_icon

        logoutButton.setOnClickListener {
            UserDetailsManager.logout()
            finishAffinity()
            finish()
            loginRouter.openLogin()
        }

        hamburgerIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        drawerLayoutEmailTv.text = "Welcome, \n${UserDetailsManager.getLoginInfo()}"
    }




    private fun onSuccess() {
        categoriesRecyclerAdapter = CategoriesAdapter(this, viewModel.categories, this)
        categoriesRecyclerView.adapter = categoriesRecyclerAdapter
        categoriesRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
    }

    private fun onFailure () {
        showErrorDialog(getString(R.string.generic_error_string)) {
            finish()
        }
    }

    override fun onItemClick(position: Int) {
        viewModel.categories.getOrNull(position)?.name?.let{
            router.openVideoPlayback(it)
        }
    }
}
