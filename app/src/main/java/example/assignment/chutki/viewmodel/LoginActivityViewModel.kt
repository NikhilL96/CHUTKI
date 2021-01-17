package example.assignment.chutki.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.assignment.chutki.model.UserDetailsModel
import example.assignment.chutki.repository.UserDetailsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivityViewModel @ViewModelInject constructor(val repository: UserDetailsRepository) :
    ViewModel() {

    fun fetchUser(
        email: String,
        onSuccess: (UserDetailsModel) -> Unit,
        onFailure: (Exception) -> Unit
    ) =
        viewModelScope.launch {
            try {
                val userDetails = repository.getUseDetails(email)

                if(userDetails == null)
                    throw UserNotFoundException()

                onSuccess.invoke(userDetails)
            } catch (exception: Exception) {
                onFailure.invoke(exception)
            }
        }
}

class UserNotFoundException: Exception()