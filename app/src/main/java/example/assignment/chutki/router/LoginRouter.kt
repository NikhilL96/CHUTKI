package example.assignment.chutki.router

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import example.assignment.chutki.view.activity.CategoryActivity
import example.assignment.chutki.view.activity.LoginActivity
import example.assignment.chutki.view.activity.RegisterUserActivity
import javax.inject.Inject


class LoginRouterImpl @Inject constructor(private val activity: FragmentActivity)
    : LoginRouter {

    override fun openLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }

    override fun openRegister() {
        val intent = Intent(activity, RegisterUserActivity::class.java)
        activity.startActivity(intent)
    }

    override fun openCategoryList() {
        val intent = Intent(activity, CategoryActivity::class.java)
        activity.startActivity(intent)
    }
}

interface LoginRouter{
    fun openLogin()
    fun openRegister()
    fun openCategoryList()

}