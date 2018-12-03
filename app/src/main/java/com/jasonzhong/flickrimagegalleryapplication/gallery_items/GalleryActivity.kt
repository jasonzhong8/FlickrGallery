package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ToggleButton
import com.jasonzhong.flickrimagegalleryapplication.R
import com.jasonzhong.flickrimagegalleryapplication.gallery_item_detail.GalleryItemDetailActivity
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.util.Util
import kotlinx.android.synthetic.main.gallery_items.*
import java.util.*

class GalleryActivity : AppCompatActivity(), ItemsContract.ItemsView,
    ItemsContract.ItemsPresenter.OnItemsLoadingFinishedListener,
    ItemsContract.ItemsPresenter.OnSearchItemsLoadingFinishedListener {

    private var galleryAdapter: GalleryAdapter? = null
    private var itemsPresenter: ItemsPresenterImpl? = null
    private val itemList = ArrayList<PhotoData>()
    private var selectedIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_items)

        search_toggleButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val on = (v as ToggleButton).isChecked

                if (on) {
                    search()
                } else {
                    itemsPresenter!!.requestAllItemsFromServer(this@GalleryActivity)
                }
            }
        })

        gallery_gridview!!.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val intent = Intent(
                this@GalleryActivity,
                GalleryItemDetailActivity::class.java
            )
            selectedIndex = position
            intent.putExtra("PhotoData", itemList[position])
            startActivity(intent)
        }
        galleryAdapter = GalleryAdapter(this, itemList)
        gallery_gridview!!.adapter = galleryAdapter
        itemsPresenter = ItemsPresenterImpl(this)
        itemsPresenter!!.requestAllItemsFromServer(this)

    }

    override fun onResume() {
        super.onResume()
    }


    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
    }


    override fun onFailure(message: String) {
        Util.showErrorDialog(
            this,
            resources.getString(R.string.exit),
            resources.getString(R.string.error),
            resources.getString(R.string.no_data_found)
        )
    }

    override fun onFinished(photoData: ArrayList<PhotoData>) {
        itemList.clear()
        itemList.addAll(photoData)
        galleryAdapter!!.notifyDataSetChanged()
    }

    override fun onSearchFailure(message: String) {
        Util.showErrorDialog(
            this,
            resources.getString(R.string.exit),
            resources.getString(R.string.error),
            resources.getString(R.string.no_data_found)
        )
    }

    override fun onSearchFinished(photoData: ArrayList<PhotoData>) {
        Util.hideSoftKeyboard(this)
        itemList.clear()
        itemList.addAll(photoData)
        galleryAdapter!!.notifyDataSetChanged()
    }

    private fun search() {
        Util.hideSoftKeyboard(this)
        itemsPresenter!!.requestSearchItemsFromServer(search_editText!!.text.toString(), itemList, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        itemsPresenter!!.addFavourite(itemList, this@GalleryActivity)
        itemsPresenter!!.onDestroy()
    }

}
