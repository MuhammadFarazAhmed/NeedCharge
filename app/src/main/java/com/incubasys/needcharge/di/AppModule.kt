package com.incubasys.needcharge.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.incubasys.needcharge.BuildConfig
import com.incubasys.needcharge.R
import com.incubasys.needcharge.di.activitylevel.RepositoryModule
import com.incubasys.needcharge.di.scope.AppScope
import com.incubasys.needcharge.di.scope.Base
import com.incubasys.needcharge.di.scope.Google


import incubasys.needcharge.repositories.remote.client.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import incubasys.needcharge.base.utils.Constants
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Retrofit

import java.util.concurrent.TimeUnit

@Module(includes = [RepositoryModule::class]) class AppModule {
    
    @Provides @Base @AppScope internal fun retrofit(gson: Gson,
                                                    @Base client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonCustomConverterFactory.create(gson)).client(client).build()
    }
    
    @Provides @Base @AppScope internal fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                                                        @Base keyAuth: ApiKeyAuth,
                                                        sessionAuth: SessionAuth): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).addInterceptor(keyAuth)
                .addInterceptor(sessionAuth).build()
    }
    
    @Provides @Google @AppScope internal fun retrofitGoogle(gson: Gson,
                                                            @Google client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonCustomConverterFactory.create(gson)).client(client).build()
    }
    
    @Provides @Google @AppScope
    internal fun okHttpClientGoogle(loggingInterceptor: HttpLoggingInterceptor,
                                    @Google keyAuth: ApiKeyAuth): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).addInterceptor(keyAuth).build()
    }
    
    @Provides @AppScope internal fun gson(): Gson {
        return GsonBuilder().setDateFormat(Constants.DATE_FORMAT_PATTERN)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
                .setPrettyPrinting().create()
    }
    
    @Provides @AppScope internal fun parseErrors(gson: Gson): ParseErrors {
        return ParseErrors(gson)
    }
    
    @Provides @Google @AppScope internal fun apiKeyAuthGoogle(context: Context): ApiKeyAuth {
        val apiKeyAuth = ApiKeyAuth("query", "key")
        apiKeyAuth.apiKey = BuildConfig.GoolgeAPIKey
        return apiKeyAuth
    }
    
    @Provides @Base @AppScope internal fun apiKeyAuth(): ApiKeyAuth {
        val apiKeyAuth = ApiKeyAuth("header", "api")
        apiKeyAuth.apiKey = BuildConfig.API_KEY
        return apiKeyAuth
    }
    
    @Provides @AppScope internal fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
    
    @Provides @AppScope internal fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
    }
    
    @Provides @AppScope
    internal fun preferencesHelper(preferences: SharedPreferences): PreferencesHelper {
        return PreferencesHelper(preferences)
    }
    
    @Provides internal fun context(application: Application): Context {
        return application.applicationContext
    }
    
}
