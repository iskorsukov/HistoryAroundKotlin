package com.iskorsukov.historyaround.injection

import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Qualifier
import kotlin.reflect.KClass

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseWikiUrlFormatString

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LanguageString

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionsList

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)