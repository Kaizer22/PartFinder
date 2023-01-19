package ru.desh.partfinder.core.domain.repository

import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface BusinessNewsRepository {
    // Remote
    suspend fun getLatestBusinessNews(pageSize: Int, pageNumber: Int): DataOrErrorResult<
            List<BusinessArticle>?, Exception?>

    // Local
    fun storeArticles(articles: List<BusinessArticle>)
    fun clearStoredArticles()
}