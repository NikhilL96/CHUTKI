package example.assignment.chutki.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.extension.StringExtensions.validateEmail
import example.assignment.chutki.extension.StringExtensions.validatePassword
import example.assignment.chutki.extension.UIExtensions.afterTextChanged
import example.assignment.chutki.router.LoginRouter
import example.assignment.chutki.viewmodel.RegisterUserActivityViewModel
import kotlinx.android.synthetic.main.activity_register_user.*
import java.lang.Exception
import javax.inject.Inject


@AndroidEntryPoint
class RegisterUserActivity : BaseActivity<RegisterUserActivityViewModel>(false) {

    override val layoutId: Int
        get() = R.layout.activity_register_user
    override val viewModelType
        get() = RegisterUserActivityViewModel::class.java


    private lateinit var registerButton: MaterialButton
    private lateinit var emailTextLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordTextLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var verifyPasswordTextLayout: TextInputLayout
    private lateinit var verifyPasswordEditText: TextInputEditText
    private lateinit var existingUser: TextView
    @Inject
    lateinit var router: LoginRouter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        registerButton = register_button
        emailTextLayout = register_email_text_layout
        emailEditText = register_email_edit_text
        passwordTextLayout = register_password_text_layout
        passwordEditText = register_password_edit_text
        verifyPasswordTextLayout = register_confirm_password_text_layout
        verifyPasswordEditText = register_confirm_password_edit_text
        existingUser = new_user_login_tv
        initUIListeners()
    }

    private fun initUIListeners() {
        registerButton.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                emailEditText.text?.toString()?.let { email ->
                    passwordEditText.text?.toString()?.let { password ->
                        viewModel.insertUser(email, password, onSuccess = {
                            finishAffinity()
                            router.openCategoryList()
                        },this::onFailure)
                    }
                }
            }
        }

        existingUser.setOnClickListener {
            finish()
            finishAffinity()
            router.openLogin()
        }

        emailEditText.afterTextChanged {
            emailTextLayout.error = null
        }

        passwordEditText.afterTextChanged {
            passwordTextLayout.error = null
            verifyPasswordTextLayout.error = null
        }

        verifyPasswordEditText.afterTextChanged {
            verifyPasswordTextLayout.error = null
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
            passwordEditText.text.isNullOrBlank() && verifyPasswordEditText.text.isNullOrBlank() -> {
                passwordTextLayout.error = getString(R.string.password_mandatory_error)
                verifyPasswordTextLayout.error = getString(R.string.password_mandatory_error)
                false
            }
            passwordEditText.text?.toString() != verifyPasswordEditText.text?.toString() -> {
                passwordTextLayout.error = getString(R.string.passwords_mismatch_error)
                verifyPasswordTextLayout.error = getString(R.string.passwords_mismatch_error)
                false
            }
            passwordEditText.text?.toString()?.validatePassword() != true -> {
                passwordTextLayout.error = getString(R.string.password_criteria_error)
                verifyPasswordTextLayout.error = getString(R.string.password_criteria_error)
                false
            }
            else -> {
                passwordTextLayout.error = null
                verifyPasswordTextLayout.error = null
                true
            }
        }
    }

    private fun onFailure(exception: Exception) = when(exception) {
        is SQLiteConstraintException -> {
            emailTextLayout.error = getString(R.string.existing_user_error)
        }
        else -> {
            showErrorDialog(getString(R.string.register_error)) {
                finish()
            }
        }
    }
}