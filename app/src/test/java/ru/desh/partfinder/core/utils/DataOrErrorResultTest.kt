package ru.desh.partfinder.core.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions
import ru.desh.partfinder.core.utils.exception.IncorrectDataOrErrorResultException

class DataOrErrorResultTest {

    @Test
    fun testDataOrErrorResult() {
        val exception = Exception("Expected exception")
        val data = 100

        Assertions.assertThrows(IncorrectDataOrErrorResultException::class.java) {
            DataOrErrorResult(null, null)
        }

        var dataOrErrorResult = DataOrErrorResult(data, exception)
        Assertions.assertEquals(true, dataOrErrorResult.isException)

        dataOrErrorResult = DataOrErrorResult(null, exception)
        Assertions.assertEquals(true, dataOrErrorResult.isException)

        dataOrErrorResult = DataOrErrorResult(data, null)
        Assertions.assertEquals(false, dataOrErrorResult.isException)

        dataOrErrorResult.setData(data)
        Assertions.assertEquals(false, dataOrErrorResult.isException)
        Assertions.assertEquals(null, dataOrErrorResult.exception)

        dataOrErrorResult.setException(exception)
        Assertions.assertEquals(true, dataOrErrorResult.isException)
        Assertions.assertEquals(null, dataOrErrorResult.data)
    }
}