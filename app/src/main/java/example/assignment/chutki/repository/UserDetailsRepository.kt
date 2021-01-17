package example.assignment.chutki.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import example.assignment.chutki.dao.UserDetailsDAO
import example.assignment.chutki.model.UserDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ActivityRetainedScoped
interface UserDetailsRepository {
    suspend fun insertUser(userDetailsModel: UserDetailsModel)
    suspend fun getUseDetails(email:String): UserDetailsModel
    suspend fun getAllRegisteredUsers(): List<UserDetailsModel>
}

@ActivityRetainedScoped
class UserDetailsRepositoryImpl @Inject constructor(private val dao: UserDetailsDAO): UserDetailsRepository {
    override suspend fun insertUser(userDetailsModel: UserDetailsModel) = withContext(Dispatchers.IO)  {
        dao.insertUser(userDetailsModel)
    }

    override suspend fun getUseDetails(email: String): UserDetailsModel = withContext(Dispatchers.IO) {
        return@withContext dao.getUserDetails(email)
    }

    override suspend fun getAllRegisteredUsers(): List<UserDetailsModel> = withContext(Dispatchers.IO) {
        return@withContext dao.getAllRegisteresUsers()
    }
}