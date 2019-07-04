package my.projects.historyaroundkotlin.injection

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import my.projects.historyaroundkotlin.service.location.LocationSource
import my.projects.historyaroundkotlin.service.location.LocationSourceImpl
import javax.inject.Singleton

@Module
class LocationModule {
    @Provides
    fun providesFusedLocationProvider(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}