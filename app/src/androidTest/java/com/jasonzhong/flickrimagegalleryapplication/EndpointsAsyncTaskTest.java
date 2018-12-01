package com.jasonzhong.flickrimagegalleryapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.jasonzhong.flickrimagegalleryapplication.model.DownloadStatus;
import com.jasonzhong.flickrimagegalleryapplication.model.PhotoData;
import com.jasonzhong.flickrimagegalleryapplication.network.GetFlickrJsonData;
import com.jasonzhong.flickrimagegalleryapplication.network.RawJsonData;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest {

    Context context;

    @Test
    public void testVerifyFlickrDataLoading() throws InterruptedException {
        assertTrue(true);
        final CountDownLatch latch = new CountDownLatch(1);
        context = InstrumentationRegistry.getContext();

        GetFlickrJsonData.OnDataAvailable callback = new GetFlickrJsonData.OnDataAvailable() {

            @Override
            public void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status) {

            }
        };
        GetFlickrJsonData testTask = new GetFlickrJsonData("en-us", true, callback) {
            @Override
            protected void onPostExecute(ArrayList<PhotoData> result) {
                assertNotNull(result);
                if (result != null) {
                    assertTrue(result.size() > 0);
                    latch.countDown();
                }
            }
        };
        testTask.execute("");
        latch.await();
    }
}
