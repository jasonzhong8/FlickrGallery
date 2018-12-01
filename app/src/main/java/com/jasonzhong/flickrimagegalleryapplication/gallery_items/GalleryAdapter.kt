package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jasonzhong.flickrimagegalleryapplication.R
import com.jasonzhong.flickrimagegalleryapplication.model.GlideApp
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.util.Util
import kotlinx.android.synthetic.main.item_layout.view.*

class GalleryAdapter(private val context: Activity, private val itemlist: List<PhotoData>) : BaseAdapter() {

    private val layoutinflater: LayoutInflater

    init {
        layoutinflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return itemlist.size
    }

    override fun getItem(i: Int): Any {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        var view : View
        var listViewHolder: ViewHolder

        if (convertView != null) {
            view = convertView;
            listViewHolder = view.tag as ViewHolder
        } else {
            view = layoutinflater.inflate(R.layout.item_layout, viewGroup, false)
            listViewHolder = ViewHolder(view)

            listViewHolder.title_textView = view.findViewById(R.id.title_textView) as TextView?
            listViewHolder.author_textView = view.findViewById(R.id.author_textView) as TextView?
            listViewHolder.thumbnail_imageView = view.findViewById(R.id.thumbnail_imageView) as ImageView?

            view!!.tag = listViewHolder
        }

        val data = itemlist[i]
        listViewHolder.title_textView!!.text = data!!.title
        listViewHolder.author_textView!!.text = data.author

        val image_width = Util.getScreenWidthPixels(context)
        val image_hight = (image_width / 3).toInt()
        listViewHolder.thumbnail_imageView!!.layoutParams = FrameLayout.LayoutParams(image_width, image_hight)
        listViewHolder.thumbnail_imageView!!.scaleType = ImageView.ScaleType.CENTER_CROP

        GlideApp.with(context)
            .load(data.image)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade()) //Optional
            .skipMemoryCache(true)  //No memory cache
            .diskCacheStrategy(DiskCacheStrategy.NONE)   //No disk cache
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(@Nullable e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    view!!.thumbnail_imageView.setImageResource(R.drawable.no_image_icon);
                    return true
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(view!!.thumbnail_imageView)

        return view
    }

    internal class ViewHolder (view: View){
        var title_textView: TextView? = null
        var author_textView: TextView? = null
        var thumbnail_imageView: ImageView? = null
    }
}

