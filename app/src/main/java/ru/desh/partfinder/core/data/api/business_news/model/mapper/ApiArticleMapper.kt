package ru.desh.partfinder.core.data.api.business_news.model.mapper

import android.icu.text.SimpleDateFormat
import ru.desh.partfinder.R
import ru.desh.partfinder.core.data.api.business_news.ApiUtils
import ru.desh.partfinder.core.data.api.business_news.model.ApiArticle
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.utils.BaseMapper
import java.util.*
import javax.inject.Inject

class ApiArticleMapper @Inject constructor() : BaseMapper<ApiArticle, BusinessArticle> {
    override fun mapToDomain(entity: ApiArticle): BusinessArticle {
        val publicationDate = SimpleDateFormat(ApiUtils.DATETIME_FORMAT, Locale.getDefault())
            .parse(entity.publishedAt.orEmpty())
        return BusinessArticle(
            source = entity.source?.name ?: R.string.unknown_source.toString(),
            author = entity.author ?: R.string.unknown_author.toString(),
            title = entity.title ?: R.string.no_title.toString(),
            description = entity.description ?: R.string.no_description.toString(),
            url = entity.url ?: "",
            urlToImage = entity.urlToImage ?: "",
            publishedAt = publicationDate
        )
    }

    override fun mapFromDomain(entity: BusinessArticle): ApiArticle {
        // Nothing to map from domain since app doesn't sending any info through NewsAPI
        TODO("Not yet implemented")
    }
}