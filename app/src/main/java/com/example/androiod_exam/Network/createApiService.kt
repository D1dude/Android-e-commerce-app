package com.example.androiod_exam.Network

import com.example.androiod_exam.Network.ApiService
import com.example.androiod_exam.Network.RetrofitBuilder

fun createApiService(): ApiService {
    return RetrofitBuilder.apiService
}