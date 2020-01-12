package com.iskorsukov.historyaround.injection

import android.Manifest
import dagger.Module
import dagger.Provides

@Module
class PermissionsModule {
    @Provides
    @PermissionsList
    fun providesPermissions(): List<String> {
        return listOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE)
    }
}