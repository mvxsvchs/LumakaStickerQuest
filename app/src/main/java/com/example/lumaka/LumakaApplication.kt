package com.example.lumaka

import android.app.Application
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LumakaApplication : Application() {
    @Inject lateinit var sessionRepository: SessionRepository

    override fun onCreate() {
        super.onCreate()
        val storedUser = sessionRepository.loadUser()
        if (storedUser != null) {
            UserSession.update(storedUser)
        }
    }
}
