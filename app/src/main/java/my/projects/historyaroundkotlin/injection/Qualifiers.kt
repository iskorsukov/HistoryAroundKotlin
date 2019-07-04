package my.projects.historyaroundkotlin.injection

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseWikiUrlFormatString

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LanguageString