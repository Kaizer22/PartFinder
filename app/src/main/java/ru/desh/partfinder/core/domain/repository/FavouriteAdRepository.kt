package ru.desh.partfinder.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.utils.DataOrErrorResult


interface FavouriteAdRepository {
    fun addToFavourite(ad: Ad): Flow<DataOrErrorResult<Boolean, Exception>>
    fun getByQuery(query: String): Flow<DataOrErrorResult<List<Ad>, Exception>>
    fun getAll(): Flow<DataOrErrorResult<List<Ad>, Exception>>
    fun deleteFromFavourites(adUid: String): Flow<DataOrErrorResult<Boolean, Exception>>
}