package example.assignment.chutki.view.activity

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.UserDetailsManager
import example.assignment.chutki.extension.StringExtensions.validateEmail
import example.assignment.chutki.extension.StringExtensions.validatePassword
import example.assignment.chutki.extension.UIExtensions.afterTextChanged
import example.assignment.chutki.router.LoginRouter
import example.assignment.chutki.viewmodel.LoginActivityViewModel
import example.assignment.chutki.viewmodel.UserNotFoundException
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginActivityViewModel>(false) {

    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModelType: Class<LoginActivityViewModel>
        get() = LoginActivityViewModel::class.java

    private lateinit var loginButton: MaterialButton
    private lateinit var emailTextLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordTextLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var newUser: TextView

    @Inject
    lateinit var router: LoginRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        loginButton = login_button
        emailTextLayout = login_email_text_layout
        emailEditText = login_email_edit_text
        passwordTextLayout = login_password_text_layout
        passwordEditText = login_password_edit_text
        newUser = new_user_register_tv
        initUIListeners()
    }

    private fun initUIListeners() {
        loginButton.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                emailEditText.text?.toString()?.let { email ->
                    passwordEditText.text?.toString()?.let { password ->
                        viewModel.fetchUser(email, onSuccess = {
                            if (password == it.password) {
                                finishAffinity()
                                UserDetailsManager.setLoginInfo(email)
                                router.openCategoryList()
                            } else {
                                emailTextLayout.error = getString(R.string.incorrect_creds_error)
                            }
                        }, this::onFailure)
                    }
                }
            }
        }

        newUser.setOnClickListener {
            finish()
            finishAffinity()
            router.openRegister()
        }

        emailEditText.afterTextChanged {
            emailTextLayout.error = null
        }

        passwordEditText.afterTextChanged {
            passwordTextLayout.error = null
        }
    }

    private fun validateEmail(): Boolean {
        return when {
            emailEditText.text?.toString().isNullOrBlank() -> {
                emailTextLayout.error = getString(R.string.email_mandatory_error)
                false
            }
            emailEditText.text?.toString()?.validateEmail() == true -> {
                emailTextLayout.error = null
                true
            }
            else -> {
                emailTextLayout.error = getString(R.string.invalid_email_error)
                false
            }
        }
    }

    private fun validatePassword(): Boolean {
        return when {
            passwordEditText.text.isNullOrBlank() -> {
                passwordTextLayout.error = getString(R.string.password_mandatory_error)
                false
            }
            passwordEditText.text?.toString()?.validatePassword() != true -> {
                passwordTextLayout.error = getString(R.string.password_criteria_error)
                false
            }
            else -> {
                passwordTextLayout.error = null
                true
            }
        }
    }

    private fun onFailure(exception: Exception) = when (exception) {
        is UserNotFoundException -> {
            emailTextLayout.error = getString(R.string.not_registered_error)
        }
        else -> {
            showErrorDialog(getString(R.string.login_failure_error)) {
                finish()
            }
        }
    }
}