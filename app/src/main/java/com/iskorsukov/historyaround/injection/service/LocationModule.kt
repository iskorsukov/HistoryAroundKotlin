package com.iskorsukov.historyaround.injection.service

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class LocationModule {
    @Provides
    fun providesFusedLocationProvider(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}