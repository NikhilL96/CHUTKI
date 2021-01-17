package example.assignment.chutki.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.router.CategoryActivityRouter
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
    @Inject lateinit var router: CategoryActivityRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        viewModel.getCategories(this::onSuccess, this::onFailure)
    }

    private fun initUI() {
        categoriesRecyclerView = categories_recycler_view
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
