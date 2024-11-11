package com.andyslab.geobud.data.repository

import android.content.Context
import android.graphics.drawable.Drawable

import com.andyslab.geobud.data.remote.RetrofitInstance
import com.andyslab.geobud.data.app.Countries
import com.andyslab.geobud.data.app.LandmarksPack.Companion.LANDMARKS_PACK
import com.andyslab.geobud.data.model.PlayerModel
import com.andyslab.geobud.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow


import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.random.Random

@GlideModule
class LandmarksRepoImpl(private val context: Context) : LandmarksRepository, AppGlideModule(){

}