package example.assignment.chutki.dao

import androidx.room.*
import example.assignment.chutki.model.UserDetailsModel

@Dao
interface UserDetailsDAO {
    @Query("SELECT * FROM userdetailsmodel")
    suspend fun getAllRegisteresUsers(): List<UserDetailsModel>

    @Query("SELECT EXISTS(SELECT * FROM userdetailsmodel WHERE email IN (:userEmail))")
    suspend fun isEmailPresent(userEmail: String): Boolean

    @Insert
    suspend fun insertUser(user: UserDetailsModel)

    @Query("SELECT * FROM userdetailsmodel WHERE email IN (:userEmail)")
    suspend fun getUserDetails(userEmail: String): UserDetailsModel

}

@Database(entities = [UserDetailsModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDetailsDao(): UserDetailsDAO
}