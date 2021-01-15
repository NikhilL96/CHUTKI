package example.assignment.chutki.view.activity

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<T : ViewModel>: AppCompatActivity() {

    protected abstract val layoutId: Int
    protected abstract val viewModelType: Class<T>
    protected lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(viewModelType)

        setContentView(layoutId)
        setListeners()
        makeActivityFullScreen()
    }

    private fun setListeners() {
        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            makeActivityFullScreen()
            insets
        }
    }

    private fun makeActivityFullScreen() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        }
        supportActionBar?.hide()
    }

    fun showErrorDialog(error: String, positiveAction:() -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage(error)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                dialog.dismiss()
                positiveAction.invoke()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}