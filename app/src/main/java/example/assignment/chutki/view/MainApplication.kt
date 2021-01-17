package example.assignment.chutki.view

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        appContext = applicationContext
    }
}