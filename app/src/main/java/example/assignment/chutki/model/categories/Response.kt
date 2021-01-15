package example.assignment.chutki.model.categories


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("videoCategories")
    var videoCategories: Map<String, Category>? = null
)