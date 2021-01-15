package example.assignment.chutki.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.assignment.chutki.model.videos.Video
import example.assignment.chutki.repository.VideoCategoryInfoRepository
import kotlinx.coroutines.launch

class VideoPlaybackActivityViewModel @ViewModelInject constructor(private val repository: VideoCategoryInfoRepository): ViewModel() {

    val videos = mutableListOf<Video>()
    val videoIdList = mutableListOf<String>()

    var categorySelected: String? = null

    fun getVideos(onSuccess:() -> Unit, onFailure: () -> Unit)  = viewModelScope.launch {
        repository.getVideos()?.let{ videoMap ->

            videoMap.keys.toList().let{videoIdsList ->
                videoIdList.addAll(videoIdsList)
            }

            videoMap.values.toList().let{videoList ->
                if(videoList.isNotEmpty()) {
                    fetchCategoryVideos(videoList)
                    onSuccess.invoke()
                    return@launch
                }
            }
        }
        onFailure.invoke()
    }

    private fun fetchCategoryVideos(allVideos: List<Video>) {
        allVideos.forEach { video ->
            video.categories?.split(",")?.forEach {category ->
                if(category == categorySelected)
                    videos.add(video)
            }
        }
    }
}