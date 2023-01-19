package ru.desh.partfinder.core.utils

import ru.desh.partfinder.core.utils.exception.ClientErrorException
import ru.desh.partfinder.core.utils.exception.RedirectException
import ru.desh.partfinder.core.utils.exception.ServerErrorException
import ru.desh.partfinder.core.utils.exception.UnknownNetworkErrorException

class NetworkUtils {
    companion object {
        fun processFailedResponse(code: Int, message: String): Exception {
            when {
                code in 301..399 -> return RedirectException("$code $message}")
                code in 401..499 -> return ClientErrorException("$code $message")
                code in 501..599 -> return ServerErrorException("$code $message")
                else -> return UnknownNetworkErrorException("$code $message")
            }
        }
    }
}