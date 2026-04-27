package com.danieloliveira138.criptoviewer.core.utils

fun String.toNormalizedString(): String {
    return when {
        this.contains("https://") -> this
        !this.startsWith("www.") -> "https://www.$this"
        else -> "https://$this"
    }
}