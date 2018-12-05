package com.jasonzhong.flickrimagegalleryapplication.gallery_item_detail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jasonzhong.flickrimagegalleryapplication.R
import com.jasonzhong.flickrimagegalleryapplication.model.GlideApp
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.util.Constant
import com.jasonzhong.flickrimagegalleryapplication.util.Util
import kotlinx.android.synthetic.main.activity_gallery_item_detail.*
import kotlinx.android.synthetic.main.content_gallery_item_detail.*


class GalleryItemDetailActivity : AppCompatActivity() {

    var mResource: Drawable? = null
    var photoData: PhotoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_item_detail)
        photoData = intent.getSerializableExtra("PhotoData") as PhotoData

        if (photoData != null) {
            detailProgressBar?.visibility = View.VISIBLE
            setData(photoData)
        }
    }

    private fun setData(photoData: PhotoData?) {
        if (photoData?.big_img_url != null) {
            GlideApp.with(this)
                .load(photoData.big_img_url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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
                        detailProgressBar?.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        mResource = resource;
                        detailProgressBar?.visibility = View.GONE
                        return false
                    }
                })
                .into(bigimage_imageView)
        } else {
            detailProgressBar?.visibility = View.GONE
        }
        title_textView?.text = resources.getString(R.string.title) + photoData?.title
        author_textView?.text = resources.getString(R.string.author) + photoData?.author
        published_textView?.text = resources.getString(R.string.published) + photoData?.published
        date_taken_textView?.text = resources.getString(R.string.date_taken) + photoData?.dateTaken
        tags_textView?.text = resources.getString(R.string.tags) + photoData?.tags
        image_textView?.text = resources.getString(R.string.image) + photoData?.image

        share_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (Util.checkPermission(this@GalleryItemDetailActivity)) {
                    share();
                } else {
                    Util.requestPermission(this@GalleryItemDetailActivity);
                }
            }
        })
    }

    fun share() {
        val mBitmap = (mResource as BitmapDrawable).bitmap;

        val path = MediaStore.Images.Media.insertImage(contentResolver, mBitmap, "Image Description", null)
        val uri = Uri.parse(path)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Flickr Image")
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // If this is our permission request result.
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            share();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
