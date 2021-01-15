package example.assignment.chutki.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.assignment.chutki.model.categories.Category
import example.assignment.chutki.model.videos.Video
import example.assignment.chutki.repository.VideoCategoryInfoRepository
import kotlinx.coroutines.launch

class CategoryActivityViewModel @ViewModelInject constructor(private val repository: VideoCategoryInfoRepository): ViewModel() {

    val categories = mutableListOf<Category>()
    val videos = mutableListOf<Video>()

    fun getCategories(onSuccess:() -> Unit, onFailure:() -> Unit)  = viewModelScope.launch {
        repository.getCategories()?.values?.toList()?.let{
            if(it.isNotEmpty()) {
                categories.clear()
                categories.addAll(it)
                onSuccess.invoke()
                return@launch
            }
        }
        onFailure.invoke()
    }

}