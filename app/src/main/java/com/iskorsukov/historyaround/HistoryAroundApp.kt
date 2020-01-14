package com.iskorsukov.historyaround

import android.app.Application
import com.iskorsukov.historyaround.injection.AppComponent
import com.iskorsukov.historyaround.injection.AppModule
import com.iskorsukov.historyaround.injection.DaggerAppComponent

class HistoryAroundApp : Application() {

    public lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}