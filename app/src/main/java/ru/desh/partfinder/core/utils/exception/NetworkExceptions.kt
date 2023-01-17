package ru.desh.partfinder.core.utils.exception

class RedirectException(message: String): Exception(message)
class ClientErrorException(message: String): Exception(message)
class ServerErrorException(message: String): Exception(message)
class UnknownNetworkErrorException(message: String): Exception(message)