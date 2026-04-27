package com.danieloliveira138.criptoviewer.core.exceptions

class EmptyResponseException : Exception() {
    override val message: String = "The server returned an empty response"
}