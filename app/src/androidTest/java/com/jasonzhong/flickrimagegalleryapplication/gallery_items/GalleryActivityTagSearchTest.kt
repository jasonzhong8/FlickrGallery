package com.jasonzhong.flickrimagegalleryapplication.gallery_items


import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.jasonzhong.flickrimagegalleryapplication.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId

@RunWith(AndroidJUnit4::class)
@LargeTest
class GalleryActivityTagSearchTest {

    @Rule
    var activityTestRule = ActivityTestRule(GalleryActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun testTagSearch() {
        onView(withId(R.id.search_editText))
            .perform(typeText("samedayedit"))

        onView(withId(R.id.search_toggleButton))
            .perform(click())
    }


}
