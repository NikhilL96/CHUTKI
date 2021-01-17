package example.assignment.chutki.extension

import java.util.regex.Pattern

object StringExtensions {

    fun String.validateEmail(): Boolean {
        return Pattern.matches(
            "\\S+@[a-zA-Z0-9]+\\.[a-zA-Z][a-zA-Z]+",
            this)
    }

    fun String.validatePassword(): Boolean {
        return Pattern.matches(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            this)
    }
}