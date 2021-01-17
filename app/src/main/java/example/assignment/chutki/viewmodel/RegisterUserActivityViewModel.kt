package example.assignment.chutki.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.assignment.chutki.model.UserDetailsModel
import example.assignment.chutki.repository.UserDetailsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterUserActivityViewModel @ViewModelInject constructor(val repository: UserDetailsRepository) : ViewModel() {

    fun insertUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) =
        viewModelScope.launch {
            try {
                repository.insertUser(UserDetailsModel(email, password))
                onSuccess.invoke()
            } catch (exception: Exception) {
                onFailure.invoke(exception)
            }
        }
}