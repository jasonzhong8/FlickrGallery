package com.jasonzhong.flickrimagegalleryapplication.model


import java.io.Serializable

class PhotoData(

    val author: String,
    val author_id: String,
    val title: String,
    val dateTaken: String,
    val published: String,
    val tags: String,
    val big_img_url: String,
    val image: String,
    var isFavourite: Boolean

) : Serializable {

    override fun toString(): String {
        return "PhotoData{" +
                ", title='" + title + '\n'.toString() +
                ", author='" + author + '\n'.toString() +
                ", author_id='" + author_id + '\n'.toString() +
                ", dateTaken='" + dateTaken + '\n'.toString() +
                ", published='" + published + '\n'.toString() +
                ", tags='" + tags + '\n'.toString() +
                ", big_img_url='" + big_img_url + '\n'.toString() +
                ", image='" + image + '\n'.toString() +
                '}'.toString()
    }

    companion object {

        private const val serialVersionUID = 1L
    }
}