package com.jasonzhong.flickrimagegalleryapplication.gallery_items


import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.jasonzhong.flickrimagegalleryapplication.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GalleryDetailActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(GalleryActivity::class.java)

    @Test
    fun galleryDetailActivityTest() {
        val frameLayout = onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.gallery_gridview),
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    )
                )
            )
            .atPosition(0)
        frameLayout.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.bigimage_imageView),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.title_textView),
                withText("Title:Why Is Oil Paintings Online Uk Considered Underrated? | oil paintings online uk"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Title:Why Is Oil Paintings Online Uk Considered Underrated? | oil paintings online uk")))

        val textView2 = onView(
            allOf(
                withId(R.id.author_textView), withText("Author:nobody@flickr.com (\"painterlegend\")"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Author:nobody@flickr.com (\"painterlegend\")")))

        val textView3 = onView(
            allOf(
                withId(R.id.published_textView), withText("Published:2018-12-01T20:29:42Z"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Published:2018-12-01T20:29:42Z")))

        val textView4 = onView(
            allOf(
                withId(R.id.date_taken_textView), withText("Date Taken:2018-12-01T12:29:42-08:00"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Date Taken:2018-12-01T12:29:42-08:00")))

        val textView5 = onView(
            allOf(
                withId(R.id.tags_textView), withText("Tags:art painting oil paintings online uk sell"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Tags:art painting oil paintings online uk sell")))

        val textView6 = onView(
            allOf(
                withId(R.id.image_textView),
                withText("Image:https://farm5.staticflickr.com/4855/31195236327_aeb95afcab_m.jpg"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Image:https://farm5.staticflickr.com/4855/31195236327_aeb95afcab_m.jpg")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
