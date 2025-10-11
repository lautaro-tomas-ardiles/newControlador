package com.example.newcontrolador.exceptions

class InvalidIpException(message: String) : Exception(message)

class ConnectionTimeoutException(message: String) : Exception(message)

class DeviceNotFoundException(message: String) : Exception(message)

class ConnectionFailedException(message: String) : Exception(message)

class UnexpectedResponseException(message: String) : Exception(message)

class SendCharFailedException(message: String) : Exception(message)