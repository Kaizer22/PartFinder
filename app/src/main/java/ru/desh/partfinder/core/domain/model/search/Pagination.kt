package ru.desh.partfinder.core.domain.model.search

class Pagination(
    val pageSize: Int,
    val pageNumber: Int
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 3
    }
}