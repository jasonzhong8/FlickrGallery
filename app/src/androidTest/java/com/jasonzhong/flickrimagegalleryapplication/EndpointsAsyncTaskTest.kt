package com.jasonzhong.flickrimagegalleryapplication

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.jasonzhong.flickrimagegalleryapplication.model.DownloadStatus
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData
import com.jasonzhong.flickrimagegalleryapplication.network.GetFlickrJsonData
import com.jasonzhong.flickrimagegalleryapplication.network.RawJsonData
import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList
import java.util.concurrent.CountDownLatch

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue

@RunWith(AndroidJUnit4::class)
class EndpointsAsyncTaskTest {

    internal lateinit var context: Context

    @Test
    @Throws(InterruptedException::class)
    fun testVerifyFlickrDataLoading() {
        assertTrue(true)
        val latch = CountDownLatch(1)
        context = InstrumentationRegistry.getContext()

        val callback = object : GetFlickrJsonData.OnDataAvailable {

            override fun onDataAvailable(data: ArrayList<PhotoData>?, status: DownloadStatus?) {

            }
        }
        val testTask = object : GetFlickrJsonData("en-us", true, callback) {
            override fun onPostExecute(result: ArrayList<PhotoData>) {
                assertNotNull(result)
                if (result != null) {
                    assertTrue(result.size > 0)
                    latch.countDown()
                }
            }
        }
        testTask.execute("")
        latch.await()
    }
}
