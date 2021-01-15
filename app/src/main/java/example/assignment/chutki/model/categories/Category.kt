package example.assignment.chutki.model.categories


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("name")
    var name: String? = null
)