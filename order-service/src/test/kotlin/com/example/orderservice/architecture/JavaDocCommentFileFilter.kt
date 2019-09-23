package com.example.orderservice.architecture

/**
 * Cleans up Javadoc comments for inclusion in the software architecture model.
 */
internal class JavadocCommentFilter(private val maxCommentLength: Int?) {

    init {
        if (maxCommentLength != null && maxCommentLength < 1) {
            throw IllegalArgumentException("Maximum comment length must be greater than 0.")
        }
    }

    fun filterAndTruncate(s: String?): String? {
        var s: String? = s ?: return null

        s = s!!.replace("\\n".toRegex(), " ")
        s = s.replace("(?s)<.*?>".toRegex(), "")
        s = s.replace("\\{@link (\\S*)\\}".toRegex(), "$1")
        s = s.replace("\\{@link (\\S*) (.*?)\\}".toRegex(), "$2")

        return if (maxCommentLength != null && s.length > maxCommentLength) {
            s.substring(0, maxCommentLength - 3) + "..."
        } else {
            s
        }
    }

}