package com.jasonzhong.flickrimagegalleryapplication.gallery_item_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jasonzhong.flickrimagegalleryapplication.R
import com.jasonzhong.flickrimagegalleryapplication.model.GlideApp
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import kotlinx.android.synthetic.main.activity_gallery_item_detail.*
import kotlinx.android.synthetic.main.content_gallery_item_detail.*

class GalleryItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_item_detail)
        val photoData = intent.getSerializableExtra("PhotoData") as PhotoData

        if (photoData != null) {
            progressBar!!.visibility = View.VISIBLE
            setData(photoData)
        }
    }

    private fun setData(photoData: PhotoData) {
        if (photoData.big_img_url != null) {
            GlideApp.with(this)
                .load(photoData.big_img_url)
                .centerInside()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        // log exception
                        Log.e("TAG", "Error loading image", e)
                        progressBar!!.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar!!.visibility = View.GONE
                        return false
                    }
                })
                .into(bigimage_imageView!!)
        } else {
            progressBar!!.visibility = View.GONE
        }
        title_textView!!.text = resources.getString(R.string.title) + photoData.title
        author_textView!!.text = resources.getString(R.string.author) + photoData.author
        published_textView!!.text = resources.getString(R.string.published) + photoData.published
        date_taken_textView!!.text = resources.getString(R.string.date_taken) + photoData.dateTaken
        tags_textView!!.text = resources.getString(R.string.tags) + photoData.tags
        image_textView!!.text = resources.getString(R.string.image) + photoData.image
    }

}
