package com.pixelpulse.health.domain.model

sealed class PixelPulseResult<out T> {
    data class Success<T>(val data: T) : PixelPulseResult<T>()
    data class Error(val exception: Throwable) : PixelPulseResult<Nothing>()
    object Loading : PixelPulseResult<Nothing>()
}

inline fun <T> PixelPulseResult<T>.onSuccess(action: (value: T) -> Unit): PixelPulseResult<T> {
    if (this is PixelPulseResult.Success) action(data)
    return this
}

inline fun <T> PixelPulseResult<T>.onError(action: (exception: Throwable) -> Unit): PixelPulseResult<T> {
    if (this is PixelPulseResult.Error) action(exception)
    return this
}

inline fun <T> PixelPulseResult<T>.onLoading(action: () -> Unit): PixelPulseResult<T> {
    if (this is PixelPulseResult.Loading) action()
    return this
} 