package example.assignment.chutki.model.videos


import com.google.gson.annotations.SerializedName

data class VideosResponseModel(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("response")
    var response: Response? = null
)