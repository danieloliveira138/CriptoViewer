package com.danieloliveira138.criptoviewer.data.remote.mapper

interface Mapper <I, O> {
    fun map(input: I): O
}