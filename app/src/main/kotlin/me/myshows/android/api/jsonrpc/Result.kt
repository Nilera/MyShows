package me.myshows.android.api.jsonrpc

sealed class Result<out T, out E>

data class Ok<out T>(val value: T) : Result<T, Nothing>()
data class Err<out E>(val error: E) : Result<Nothing, E>()
