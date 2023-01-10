package ru.desh.partfinder.core.domain.repository

import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.model.search.Pagination

interface BusinessNewsRepository {
    // Remote
    suspend fun getLatestBusinessNews(pagination: Pagination): List<BusinessArticle>

    // Local
    fun storeArticles(articles: List<BusinessArticle>)
    fun clearStoredArticles()
}