package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import com.jasonzhong.flickrimagegalleryapplication.model.DownloadStatus
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.network.GetFlickrJsonData

import java.util.ArrayList

class ItemsPresenterImpl(internal var view: ItemsContract.ItemsView?) : ItemsContract.ItemsPresenter,
    GetFlickrJsonData.OnDataAvailable {

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
            onSearchItemsLoadingFinishedListener.onSearchFinished(newPhotoDataList)
        } else {
            onSearchItemsLoadingFinishedListener.onSearchFailure("Not Found")
        }

    }
}
