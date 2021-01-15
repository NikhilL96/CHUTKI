package example.assignment.chutki.model.videos


import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("categories")
    var categories: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("thumbnailURL")
    var thumbnailURL: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("videoURL")
    var videoURL: String? = null
)