package example.assignment.chutki.model.videos


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("videos")
    var videos: Map<String, Video>? = null
)