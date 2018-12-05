package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import android.R.id.edit
import android.content.SharedPreferences
import com.jasonzhong.flickrimagegalleryapplication.util.Constant


class GalleryAdapter(private val context: Activity, private val itemlist: List<PhotoData>) : BaseAdapter() {

    private val layoutinflater: LayoutInflater
    private val favourites: Set<String>

    init {
        layoutinflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        favourites = getFavourites();
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
        var view: View
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
            listViewHolder.favourite_checkbox = view.findViewById(R.id.favourite_checkbox) as CheckBox?

            view?.tag = listViewHolder
        }

        val data = itemlist[i]
        listViewHolder.title_textView?.text = data?.title
        listViewHolder.author_textView?.text = data.author
        if (isFavourite(data.author_id)) {
            listViewHolder.favourite_checkbox?.isChecked = true
        } else {
            listViewHolder.favourite_checkbox?.isChecked = false
        }

        listViewHolder.favourite_checkbox?.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                data.isFavourite = true
            } else {
                data.isFavourite = false
            }
        })

        val image_width = Util.getScreenWidthPixels(context)
        val image_hight = (image_width / 3).toInt()
        listViewHolder.thumbnail_imageView?.layoutParams = FrameLayout.LayoutParams(image_width, image_hight)
        listViewHolder.thumbnail_imageView?.scaleType = ImageView.ScaleType.CENTER_CROP

        GlideApp.with(context)
            .load(data.image)
            .centerInside()
            .transition(DrawableTransitionOptions.withCrossFade()) //Optional
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?, model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    view?.thumbnail_imageView?.setImageResource(R.drawable.no_image_icon);
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(view.thumbnail_imageView)

        return view
    }

    internal class ViewHolder(view: View) {
        var title_textView: TextView? = null
        var author_textView: TextView? = null
        var thumbnail_imageView: ImageView? = null
        var favourite_checkbox: CheckBox? = null
    }

    fun getFavourites(): Set<String> {
        val settings = context.getSharedPreferences(Constant.FAVORITE_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val Favourites = settings.getStringSet(Constant.FAVORITE, HashSet<String>())
        return Favourites;
    }

    private fun isFavourite(authorId: String): Boolean {

        var isFavourite = false
        val array = favourites?.toTypedArray()
        if (array != null && array.size > 0) {
            for (item in array) {
                if (item == authorId) {
                    isFavourite = true
                    break
                }
            }
        }
        return isFavourite
    }

}

