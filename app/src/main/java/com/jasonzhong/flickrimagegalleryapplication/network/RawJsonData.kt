package com.jasonzhong.flickrimagegalleryapplication.network

import android.os.AsyncTask
import android.util.Log
import com.jasonzhong.flickrimagegalleryapplication.model.DownloadStatus

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class RawJsonData(private val Callback: OnDownloadComplete?) : AsyncTask<String, Void, String>() {
    private var downloadStatus: DownloadStatus? = null

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String?, status: DownloadStatus?)
    }

    init {
        downloadStatus = DownloadStatus.IDLE
    }

    fun runOnSameThread(s: String) {
        Log.d(TAG, "runOnSameThread: starts")
        //        onPostExecute(doInBackground(s));
        Callback?.onDownloadComplete(doInBackground(s), downloadStatus)
        Log.d(TAG, "runOnSameThread: ends")
    }

    override fun onPostExecute(s: String) {
        //Log.d(TAG, "onPostExecute: Parameter provided: "+s);
        super.onPostExecute(s)
        Callback?.onDownloadComplete(s, downloadStatus)
        Log.d(TAG, "onPostExecute: COMPLETE")
    }


    override fun doInBackground(vararg strings: String): String? {
        var feedloader: HttpURLConnection? = null
        var bufferedReader: BufferedReader? = null
        if (strings == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return null
        }

        try {
            downloadStatus = DownloadStatus.PROCESSING
            val url = URL(strings[0])
            feedloader = url.openConnection() as HttpURLConnection
            feedloader.requestMethod = "GET"
            feedloader.connect()
            val response = feedloader.responseCode

            val builder = StringBuilder()
            bufferedReader = BufferedReader(InputStreamReader(feedloader.inputStream))
            val reader = BufferedReader(bufferedReader)
            var line: String? = null;
            while ({ line = reader.readLine(); line }() != null) {
                builder.append(line).append("\n")
            }
            downloadStatus = DownloadStatus.OK
            return builder.toString()
        } catch (e: MalformedURLException) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "doInBackground: I/O error " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "doInBackground: Unknown error " + e.message)
        } finally {
            feedloader?.disconnect()
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (e: IOException) {
                    Log.e(TAG, "doInBackground:  error in closing stream " + e.message)
                }

            }
        }
        downloadStatus = DownloadStatus.FAILED_OR_EMPTY
        return null
    }

    companion object {
        private val TAG = "RawJsonData"
    }
}
