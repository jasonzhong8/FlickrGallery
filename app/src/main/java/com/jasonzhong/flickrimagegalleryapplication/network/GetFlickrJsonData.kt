package com.jasonzhong.flickrimagegalleryapplication.network

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.jasonzhong.flickrimagegalleryapplication.model.*
import com.jasonzhong.flickrimagegalleryapplication.util.Constant
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

open class GetFlickrJsonData(lang: String, matchAll: Boolean?, private val Callback: OnDataAvailable?) :
    AsyncTask<String, Void, ArrayList<PhotoData>>(), RawJsonData.OnDownloadComplete {
    private var photoDataArrayList: ArrayList<PhotoData>? = null
    private val lang = "en-us"
    private var MatchAll = true
    private var runningOnSameThread = false

    init {
        MatchAll = matchAll!!
    }

    interface OnDataAvailable {
        fun onDataAvailable(data: ArrayList<PhotoData>?, status: DownloadStatus?)
    }

    override fun onPostExecute(photoData: ArrayList<PhotoData>) {
        Log.d(TAG, "onPostExecute: starts")
        Callback?.onDataAvailable(photoData, DownloadStatus.OK)
        Log.d(TAG, "onPostExecute: ends")
    }


    internal fun executeOnSameThread(searchCriteria: String) {
        Log.d(TAG, "executeOnSameThread: starts")
        runningOnSameThread = true
        val destinationUri = createUri(searchCriteria, MatchAll, lang)
        val rawJsonData = RawJsonData(this)
        rawJsonData.execute(destinationUri)
        Log.d(TAG, "executeOnSameThread: ends")
    }

    override fun doInBackground(vararg params: String): ArrayList<PhotoData>? {
        Log.d(TAG, "doInBackground: starts")
        val destinationUri = createUri(params[0], MatchAll, lang)
        val jsonData = RawJsonData(this)
        jsonData.runOnSameThread(destinationUri)
        Log.d(TAG, "doInBackground: ends")
        return photoDataArrayList
    }

    private fun createUri(searchCriteria: String, matchAll: Boolean, lang: String): String {
        Log.d(TAG, "createUri: starts")
        return Uri.parse(Constant.BASE_URL).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang).build().toString()
    }

    override fun onDownloadComplete(data: String?, status: DownloadStatus?) {
        if (status === DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: starts status:$status")
            photoDataArrayList = ArrayList()
            try {
                val jsonObject = JSONObject(data)
                val itemsData = jsonObject.getJSONArray("items")
                for (i in 0 until itemsData.length()) {
                    val item = itemsData.getJSONObject(i)
                    val title = item.getString("title")
                    val author = item.getString("author")
                    val authorID = item.getString("author_id")
                    val published = item.getString("published")
                    val tags = item.getString("tags")
                    val media = item.getJSONObject("media")
                    val image = media.getString("m")
                    val dateTaken = item.getString("date_taken")
                    val link = item.getString("link")
                    val big_image_link = image.replaceFirst("_m".toRegex(), "_b")
                    val photo = PhotoData(author, authorID, title, dateTaken, published, tags, big_image_link, image)
                    photoDataArrayList!!.add(photo)
                    //                Log.d(TAG, "onDownloadComplete: \n"+ photo.toString());

                }
            } catch (e: JSONException) {
                Log.e(TAG, "onDownloadComplete: unable yo parse json: " + e.message)
            }

        }
        if (runningOnSameThread && Callback != null) {
            //error
            Log.d(TAG, "onDownloadComplete: running on same thread: $runningOnSameThread")
            Callback.onDataAvailable(photoDataArrayList, status)

        } else {
            //Download Failed
            Log.e(TAG, "onDownloadComplete: Download Failed with status: " + status!!)
        }
    }

    companion object {

        private val TAG = "GetFlickrJsonData"
    }
}
