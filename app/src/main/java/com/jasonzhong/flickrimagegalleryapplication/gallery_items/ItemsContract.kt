package com.jasonzhong.flickrimagegalleryapplication.gallery_items

import android.content.Context
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import java.util.ArrayList

interface ItemsContract {

    interface ItemsPresenter {

        interface OnItemsLoadingFinishedListener {

            fun onFailure(message: String)

            fun onFinished(photoData: ArrayList<PhotoData>)
        }

        interface OnSearchItemsLoadingFinishedListener {

            fun onSearchFailure(message: String)

            fun onSearchFinished(photoData: ArrayList<PhotoData>)
        }


        fun onDestroy()

        fun requestAllItemsFromServer(
            onItemsLoadingFinishedListener: OnItemsLoadingFinishedListener
        )

        fun requestSearchItemsFromServer(
            tag: String,
            PhotoDataList: ArrayList<PhotoData>,
            onSearchItemsLoadingFinishedListener: OnSearchItemsLoadingFinishedListener
        )

        fun addFavourite(photoDataList: ArrayList<PhotoData>, context: Context)

    }

    interface ItemsView {

        fun showProgress()

        fun hideProgress()

    }
}