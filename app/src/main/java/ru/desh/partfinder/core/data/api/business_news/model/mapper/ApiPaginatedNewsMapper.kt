package ru.desh.partfinder.core.data.api.business_news.model.mapper

import ru.desh.partfinder.core.data.api.business_news.model.ApiPaginatedNews
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.utils.BaseMapper
import javax.inject.Inject

class ApiPaginatedNewsMapper @Inject constructor(
    val apiArticleMapper: ApiArticleMapper
): BaseMapper<ApiPaginatedNews, List<BusinessArticle>> {
    override fun mapToDomain(entity: ApiPaginatedNews): List<BusinessArticle> {
        return entity.articles?.map { apiArticleMapper.mapToDomain(it) } ?: listOf()
    }

    override fun mapFromDomain(entity: List<BusinessArticle>): ApiPaginatedNews {
        // Nothing to map from domain since app doesn't sending any info through NewsAPI
        TODO("Not yet implemented")
    }
}