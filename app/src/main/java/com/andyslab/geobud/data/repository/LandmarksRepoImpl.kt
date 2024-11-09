package com.andyslab.geobud.data.repository

import android.content.Context
import android.graphics.drawable.Drawable

import com.andyslab.geobud.data.remote.RetrofitInstance
import com.andyslab.geobud.data.local.Countries
import com.andyslab.geobud.data.model.PlayerDto
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
import kotlinx.coroutines.delay
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

    override fun updatePlayerData(player: PlayerDto,){
        var isNewPack = false
        var currentPack = player.packs[player.position.x]
        player.position.y++

            if(player.position.y >= currentPack.size){
                player.position.x++
                player.position.y = 0
                isNewPack = true
            }


        if((player.position.x < player.packs.size) && player.position.y < (player.packs.last().size)){
            val landmark = player.packs[player.position.x].elementAt(player.position.y)
            player.currentLandmark = landmark

            val previousPhoto = player.photoURLs.first()
            player.photoURLs.remove(previousPhoto)
            player.currentLandmarkPhoto = player.photoURLs.first()

            player.coins+=15
            player.stars+=1
        }

    }

    override suspend fun getLandmarkPhotoURLs(player: PlayerDto): Flow<Resource<MutableSet<String?>>> {
        return flow {
            var isError = false
            emit(Resource.Loading())

            //val currPack = player.packs[player.position.x]

            val result = mutableSetOf<String?>()

            try{
                for(currPack in listOf(player.packs[0], player.packs[1])){
                    for(j in 0 until(currPack.size)){
                        val page = Random.nextInt(1, 6)

                        val response = RetrofitInstance.api.getLandmarkPhoto(currPack.elementAt(j).name, page)
                        emit(Resource.Loading(data = result))

                        if(response.isSuccessful && response != null){
                            (response.body()?.photos?.get(0)?.src?.portrait).also{
                            result.add(it)
                            player.photoURLs.add(it)
                        }
                        }else{
                            emit(Resource.Error("Unsuccessful Response. Code: ${response.code()}"))
                            isError = true
                            break
                        }
                    }
                }

        }
            catch(e: IOException){
                emit(Resource.Error("Connection error. Please check your internet connection."))
                isError = true
            }
            catch(e: HttpException){
                emit(Resource.Error("Bad response. Please retry"))
                isError = true
            }

            if(result.isNotEmpty()) player.currentLandmarkPhoto = result.first()

            if(!isError){
                emit(Resource.Success(result))
            }
        }
    }

    override suspend fun checkAndUpdatePhotoURLs(player: PlayerDto): Flow<Resource<MutableSet<String?>>> {
        return flow{

       }
    }

    override suspend fun downloadAndCachePhotos(player: PlayerDto): Flow<Resource<Unit>> {
        var emitError = false
        return flow{
        emit(Resource.Loading())
            //getLandmarkPhotoURLs(player)

            player.photoURLs.forEach { url ->
            Glide.with(context)
                .downloadOnly()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .load(url)
                .listener(object: RequestListener<File>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<File>,
                        isFirstResource: Boolean
                    ): Boolean {
                        emitError = true
                        return true
                    }

                    override fun onResourceReady(
                        resource: File,
                        model: Any,
                        target: Target<File>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .submit()
                //.get()

            if(emitError){
                emit(Resource.Error("Error loading photo. Please check your internet connection."))
                currentCoroutineContext().cancel()

            }
            else{
            emit(Resource.Loading())
            }
        }
            emit(Resource.Success())
        }
    }



    override suspend fun loadPhoto(imageUrl: String?): Drawable?{
        var drawable: Drawable? = null

        suspendCancellableCoroutine<Drawable?>{ continuation ->
        Glide.with(context) // Use application context for background tasks
            .asDrawable()
            .load(imageUrl)
            .transition(withCrossFade())
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    drawable = resource
                    continuation.resume(resource)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    drawable = null
                    return false
                }
            }).submit()
        }
        return drawable
    }


    override fun generateOptions(player: PlayerDto): MutableSet<String> {
        var result = mutableSetOf<String>()
        val country = player.currentLandmark.country
        result.add(country)

        while(result.size < 4){

            if(country in Countries.AS){
                result.add(Countries.EU.random())
                result.add(Countries.AS.random())
                result.add(Countries.AS.random())
            }
            else if(country in Countries.AF){
                result.add(Countries.AF.random())
                result.add(Countries.AF.random())
                result.add(Countries.EU.random())
            }
            else if(country in Countries.EU){
                result.add(Countries.NA.random())
                result.add(Countries.EU.random())
                result.add(Countries.EU.random())
            }
            else if(country in Countries.NA){
                result.add(Countries.EU.random())
                result.add(Countries.NA.random())
                result.add(Countries.EU.random())
            }
            else if(country in Countries.SA){
                result.add(Countries.SA.random())
                result.add(Countries.EU.random())
                result.add(Countries.OC.random())
            }
            else if (country in Countries.OC){
                result.add(Countries.SA.random())
                result.add(Countries.NA.random())
                result.add(Countries.OC.random())
            }
        }

        result = result.shuffled().toMutableSet()
        player.currentOptions = result
        return result
    }
}