package com.arzhang.lifediary.connectivity

import android.net.http.UrlRequest
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe() : Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

}