package com.iskorsukov.historyaround.injection.service

import android.Manifest
import com.iskorsukov.historyaround.injection.PermissionsList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PermissionsModule {

    @Provides
    @PermissionsList
    fun providesPermissions(): List<String> {
        return listOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
    }
}