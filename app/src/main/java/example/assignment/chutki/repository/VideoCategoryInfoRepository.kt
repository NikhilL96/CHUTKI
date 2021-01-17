package example.assignment.chutki.repository

import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityRetainedScoped
import example.assignment.chutki.apiservice.VideoCategoryInfoApiService
import example.assignment.chutki.model.categories.Category
import example.assignment.chutki.model.categories.CategoryResponseModel
import example.assignment.chutki.model.videos.Video
import example.assignment.chutki.model.videos.VideosResponseModel
import javax.inject.Inject

@ActivityRetainedScoped
interface VideoCategoryInfoRepository {
    suspend fun getCategories(): Map<String, Category>?
    suspend fun getVideos(): Map<String, Video>?

}

@ActivityRetainedScoped
class VideoCategoryInfoRepositoryImpl @Inject constructor(val apiService: VideoCategoryInfoApiService,
                                                          val gson: Gson): VideoCategoryInfoRepository {
    override suspend fun getCategories(): Map<String, Category>?  {
        val reponse = apiService.getResponse("http://www.mocky.io/v2/5e2bebd23100007a00267e51")
        return gson.fromJson(reponse, CategoryResponseModel::class.java)?.response?.videoCategories
    }

    override suspend fun getVideos(): Map<String, Video>? {
        val reponse = apiService.getResponse("http://www.mocky.io/v2/5e2beb5a3100006600267e4e")
        return gson.fromJson(reponse, VideosResponseModel::class.java)?.response?.videos
    }

}

