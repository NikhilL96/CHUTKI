package example.assignment.chutki.model.categories


import com.google.gson.annotations.SerializedName

data class CategoryResponseModel(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("response")
    var response: Response? = null
)