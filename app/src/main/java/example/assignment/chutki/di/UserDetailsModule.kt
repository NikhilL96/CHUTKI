package example.assignment.chutki.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import example.assignment.chutki.dao.AppDatabase
import example.assignment.chutki.dao.UserDetailsDAO
import example.assignment.chutki.repository.UserDetailsRepository
import example.assignment.chutki.repository.UserDetailsRepositoryImpl
import example.assignment.chutki.repository.VideoCategoryInfoRepository
import example.assignment.chutki.repository.VideoCategoryInfoRepositoryImpl
import example.assignment.chutki.router.CategoryActivityRouter
import example.assignment.chutki.router.CategoryActivityRouterImpl
import example.assignment.chutki.router.LoginRouter
import example.assignment.chutki.router.LoginRouterImpl
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object UserDetailsModule {

    @InstallIn(ApplicationComponent::class)
    @Module
    object DatabaseModule {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "logging.db"
            ).build()
        }

        @Provides
        fun provideLogDao(database: AppDatabase): UserDetailsDAO {
            return database.userDetailsDao()
        }
    }
}

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class UserInfoDetailsRetainedModule {
    @Binds
    abstract fun bindRepository(impl: UserDetailsRepositoryImpl): UserDetailsRepository
}

@InstallIn(ActivityComponent::class)
@Module
abstract class UserInfoDetailsModule {
    @Binds
    abstract fun bindRouter(impl: LoginRouterImpl): LoginRouter
}
