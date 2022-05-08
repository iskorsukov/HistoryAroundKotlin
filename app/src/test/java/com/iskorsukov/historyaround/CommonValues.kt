package com.iskorsukov.historyaround

object CommonValues {

    val languageCode = "en"

    val pageids = arrayOf(1L, 2L, 3L)

    val articleTitles = arrayOf(
        "First title",
        "Second title",
        "Third title"
    )

    val articleDescriptions = arrayOf(
        "First description",
        null,
        ""
    )

    val articleCoordinatePairs = arrayOf(
        listOf(0.0 to 1.0, 2.0 to 3.0),
        null,
        emptyList()
    )

    val articleThumbnailTriples = arrayOf(
        Triple("first", 0, 1),
        Triple("second", 2, 3),
        null
    )

    val articleExtracts = arrayOf(
        "First extract",
        "",
        null
    )

    val fullUrls = arrayOf(
        "First url",
        null,
        ""
    )
}