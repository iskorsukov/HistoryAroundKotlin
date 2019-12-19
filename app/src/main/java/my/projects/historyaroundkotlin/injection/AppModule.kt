package my.projects.historyaroundkotlin.injection

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val appContext: Context) {

    @Provides
    fun providesAppContext(): Context {
        return appContext
    }
}