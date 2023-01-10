package ru.desh.partfinder.core.data.api.business_news

import ru.desh.partfinder.core.data.api.business_news.model.mapper.ApiPaginatedNewsMapper
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.model.search.Pagination
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import javax.inject.Inject

class BusinessNewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val apiPaginatedNewsMapper: ApiPaginatedNewsMapper
): BusinessNewsRepository {

    private val CATEGORY = "business"
    override suspend fun getLatestBusinessNews(pagination: Pagination): List<BusinessArticle>  {
        val articles = newsApi.getTopHeadlines(country = "ru",
            category = CATEGORY,
            pageSize = pagination.pageSize, page = pagination.pageNumber)
        return apiPaginatedNewsMapper.mapToDomain(articles)
    }

    override fun storeArticles(articles: List<BusinessArticle>) {
        TODO("Not yet implemented")
    }

    override fun clearStoredArticles() {
        TODO("Not yet implemented")
    }
}