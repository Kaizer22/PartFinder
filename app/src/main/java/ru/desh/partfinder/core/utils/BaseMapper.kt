package ru.desh.partfinder.core.utils

interface BaseMapper<E, D> {
    fun mapToDomain(entity: E): D
    fun mapFromDomain(entity: D): E
}