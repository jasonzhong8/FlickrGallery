package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import android.content.Context
import com.jasonzhong.flickrimagegalleryapplication.model.DownloadStatus
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.network.GetFlickrJsonData
import com.jasonzhong.flickrimagegalleryapplication.util.Constant
import org.apache.commons.lang3.builder.CompareToBuilder

import java.util.*


class ItemsPresenterImpl(internal var view: ItemsContract.ItemsView?) : ItemsContract.ItemsPresenter,
    GetFlickrJsonData.OnDataAvailable {


    override fun addFavourite(photoDataList: ArrayList<PhotoData>, context: Context) {
        val settings = context.getSharedPreferences(Constant.FAVORITE_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val favourites = settings.getStringSet(Constant.FAVORITE, HashSet())

        val authorIDlist = ArrayList<String>()
        for (photoData in photoDataList) {
            if (photoData.isFavourite) {
                authorIDlist.add(photoData.author_id)
            }
        }
        for (authorId in authorIDlist) {

            favourites!!.add(authorId)

        }

        editor.putStringSet(Constant.FAVORITE, favourites)
        editor.commit()
    }

    internal lateinit var onItemsLoadingFinishedListener: ItemsContract.ItemsPresenter.OnItemsLoadingFinishedListener
    internal lateinit var onSearchItemsLoadingFinishedListener: ItemsContract.ItemsPresenter.OnSearchItemsLoadingFinishedListener

    override fun onDestroy() {
        view = null
    }

    override fun requestAllItemsFromServer(onItemsLoadingFinishedListener: ItemsContract.ItemsPresenter.OnItemsLoadingFinishedListener) {
        this.onItemsLoadingFinishedListener = onItemsLoadingFinishedListener
        this.view!!.showProgress()
        val flickrJsonData = GetFlickrJsonData("en-us", true, this)
        flickrJsonData.execute("")
    }

    override fun requestSearchItemsFromServer(
        tag: String,
        dataArrayList: ArrayList<PhotoData>,
        onSearchItemsLoadingFinishedListener: ItemsContract.ItemsPresenter.OnSearchItemsLoadingFinishedListener
    ) {
        this.onSearchItemsLoadingFinishedListener = onSearchItemsLoadingFinishedListener
        searchPhotoList(tag, dataArrayList)
    }

    override fun onDataAvailable(data: ArrayList<PhotoData>?, status: DownloadStatus?) {
        this.view!!.hideProgress()
        if (data != null && data.size > 0) {
            Collections.sort(
                data,
                Collections.reverseOrder({ item1, item2 ->
                    CompareToBuilder()
                        .append(item1.dateTaken, item2.dateTaken).toComparison()
                })
            )
            onItemsLoadingFinishedListener.onFinished(data)
        } else {
            onItemsLoadingFinishedListener.onFailure("Error")
        }
    }

    private fun searchPhotoList(tag: String, dataArrayList: ArrayList<PhotoData>) {
        val newPhotoDataList = ArrayList<PhotoData>()
        for (photoData in dataArrayList) {
            if (photoData.tags == tag) {
                newPhotoDataList.add(photoData)
            }
        }
        if (newPhotoDataList.size > 0) {
            Collections.sort(
                newPhotoDataList,
                Collections.reverseOrder({ item1, item2 ->
                    CompareToBuilder()
                        .append(item1.dateTaken, item2.dateTaken).toComparison()
                })
            )
            onSearchItemsLoadingFinishedListener.onSearchFinished(newPhotoDataList)
        } else {
            onSearchItemsLoadingFinishedListener.onSearchFailure("Not Found")
        }

    }
}
