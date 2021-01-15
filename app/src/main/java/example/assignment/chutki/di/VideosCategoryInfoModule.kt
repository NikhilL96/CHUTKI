package example.assignment.chutki.di

import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import example.assignment.chutki.repository.VideoCategoryInfoRepository
import example.assignment.chutki.repository.VideoCategoryInfoRepositoryImpl
import example.assignment.chutki.router.CategoryActivityRouter
import example.assignment.chutki.router.CategoryActivityRouterImpl

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class VideoCategoryInfoRetainedModule {
    @Binds
    abstract fun bindRepository(impl: VideoCategoryInfoRepositoryImpl): VideoCategoryInfoRepository
}

@InstallIn(ActivityComponent::class)
@Module
abstract class CategoryInfoActivityModule {
    @Binds
    abstract fun bindRouter(impl: CategoryActivityRouterImpl): CategoryActivityRouter
}

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}