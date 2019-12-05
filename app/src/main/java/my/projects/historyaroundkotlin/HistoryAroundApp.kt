package my.projects.historyaroundkotlin

import android.app.Application
import android.util.Log
import my.projects.historyaroundkotlin.injection.AppComponent
import my.projects.historyaroundkotlin.injection.AppModule
import my.projects.historyaroundkotlin.injection.DaggerAppComponent

class HistoryAroundApp : Application() {

    public lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}