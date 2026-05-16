package ru.netology.nmedia.api;

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.AuthResponse

private const val AUTH_BASE_URL = "${BuildConfig.BASE_URL}/api/"


private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

// Для авторизации не добавляем interceptor с токеном — его ещё нет.
private val authOkHttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val authRetrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(AUTH_BASE_URL)
    .client(authOkHttp)
    .build()

interface AuthApiService {
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun authenticate(
        @Field("login") login: String,
        @Field("pass") password: String
    ): Response<AuthResponse>
}


object AuthApi {
    val service: AuthApiService by lazy {
        authRetrofit.create(AuthApiService::class.java)
    }
}


