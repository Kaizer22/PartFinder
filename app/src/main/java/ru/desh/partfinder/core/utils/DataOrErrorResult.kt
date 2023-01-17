package ru.desh.partfinder.core.utils
import ru.desh.partfinder.core.utils.exception.IncorrectDataOrErrorResultException

class DataOrErrorResult<T, E : Exception?>() {
    var data: T? = null
        private set
//        set(value) {
//            if (value != null) {
//                field = value
//                exception = null
//                isException = false
//            } else if (exception == null) throw EmptySetException()
//        }
    var exception: E? = null
        private set
//        set(value) {
//            if (value != null) {
//                field = value
//                data = null
//                isException = true
//            } else if (data == null) throw EmptySetException()
//        }
    
    fun setData(data: T) {
        isException = false
        this.data = data
        this.exception = null
    }
    
    fun setException(exception: E) {
        isException = true
        this.data = null
        this.exception = exception
    }
    
    var isException: Boolean = false
        private set

    constructor(data: T?, exception: E?) : this() {
        this.data = data
        this.exception = exception
        if (this.data == null && this.exception == null) {
            throw IncorrectDataOrErrorResultException()
        } else isException = this.exception != null
    }
}