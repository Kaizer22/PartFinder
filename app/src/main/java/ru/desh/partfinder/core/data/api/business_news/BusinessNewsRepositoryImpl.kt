package ru.desh.partfinder.core.data.api.business_news

import ru.desh.partfinder.core.data.api.business_news.model.ApiPaginatedNews
import ru.desh.partfinder.core.data.api.business_news.model.mapper.ApiPaginatedNewsMapper
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.core.utils.NetworkUtils.Companion.processFailedResponse
import javax.inject.Inject

class BusinessNewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val apiPaginatedNewsMapper: ApiPaginatedNewsMapper
) : BusinessNewsRepository {

    private val CATEGORY = "business"
    override suspend fun getLatestBusinessNews(pageSize: Int, pageNumber: Int):
            DataOrErrorResult<List<BusinessArticle>?, Exception?> {
        val response = newsApi.getTopHeadlines(
            country = "ru",
            category = CATEGORY,
            pageSize = pageSize, page = pageNumber
        )
        val result = DataOrErrorResult<List<BusinessArticle>?, Exception?>(emptyList(), null)
        if (response.isSuccessful) {
            val articles = apiPaginatedNewsMapper.mapToDomain(
                response.body() ?: ApiPaginatedNews(
                    totalResults = null,
                    articles = null,
                    status = null
                )
            )
            result.setData(articles)
        } else {

            result.setException(processFailedResponse(response.code(), response.message()))
        }
        return result
    }

    override fun storeArticles(articles: List<BusinessArticle>) {
        TODO("Not yet implemented")
    }

    override fun clearStoredArticles() {
        TODO("Not yet implemented")
    }
}