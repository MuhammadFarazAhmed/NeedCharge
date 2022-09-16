package com.incubasys.needcharge.di


import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.incubasys.needcharge.NeedChargeApplication
import okhttp3.OkHttpClient

import java.io.InputStream

@Excludes(com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule::class)
@GlideModule
class OkHttpLibraryGlideModule : AppGlideModule() {

    private val client: OkHttpClient? = NeedChargeApplication.getInstance()?.getAppComponent()?.okhttpClient()

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        client?.let { registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(it)) }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}