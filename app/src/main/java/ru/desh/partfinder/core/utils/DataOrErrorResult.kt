package ru.desh.partfinder.core.utils

import ru.desh.partfinder.core.utils.exception.EmptySetException
import ru.desh.partfinder.core.utils.exception.IncorrectDataOrErrorResultException

class DataOrErrorResult<T, E : Exception?>() {
    var data: T? = null
        set(value) {
            if (value != null) {
                field = value
                exception = null
                isException = false
            } else if (exception == null) throw EmptySetException()
        }
    var exception: E? = null
        set(value) {
            if (value != null) {
                field = value
                data = null
                isException = true
            } else if (data == null) throw EmptySetException()
        }
    var isException: Boolean = false
        private set

    constructor(d: T?, e: E?) : this() {
        data = d
        exception = e
        if (data == null && exception == null) {
            throw IncorrectDataOrErrorResultException()
        } else isException = data == null
    }
}