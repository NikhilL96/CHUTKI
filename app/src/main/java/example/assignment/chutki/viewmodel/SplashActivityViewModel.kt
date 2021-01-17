package example.assignment.chutki.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.assignment.chutki.model.UserDetailsModel
import example.assignment.chutki.repository.UserDetailsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashActivityViewModel @ViewModelInject constructor(val repository: UserDetailsRepository) :
    ViewModel() {

    fun fetchAllUser(
        onFetch: (atleastOneUserRegistered: Boolean) -> Unit) =
        viewModelScope.launch {
            try {
                val allUsers = repository.getAllRegisteredUsers()
                onFetch.invoke(allUsers.isNotEmpty())
            } catch (exception: Exception) {
                onFetch.invoke(false)
            }
        }
}