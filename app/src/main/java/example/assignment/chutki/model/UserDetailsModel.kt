package example.assignment.chutki.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserDetailsModel(
    @NonNull @PrimaryKey() val email: String,
    @ColumnInfo(name = "password") val password: String?
)