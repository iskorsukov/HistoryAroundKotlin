package my.projects.historyaroundkotlin.service.storage

import io.reactivex.Maybe

interface WikiSettingsSource {
    fun mapLanugageToWikiCode(language: String): Maybe<String>
}