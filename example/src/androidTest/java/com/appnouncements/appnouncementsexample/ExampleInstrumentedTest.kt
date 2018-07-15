package com.appnouncements.appnouncementsexample

import android.Manifest
import android.os.Environment
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.web.assertion.WebViewAssertions.webMatches
import android.support.test.espresso.web.model.Atoms.getTitle
import android.support.test.espresso.web.sugar.Web.onWebView
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.appnouncements.appnouncementsexample.R.id.releaseNotesButton
import com.jraska.falcon.Falcon
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File




/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Rule @JvmField
    var mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule @JvmField
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun showsReleaseNotes() {
        onView(withId(releaseNotesButton)).perform(click())
        onWebView().check(webMatches(getTitle(), containsString("Appnouncements")))
        Falcon.takeScreenshot(activityTestRule.activity, createScreenshotFile("after_release_notes_loaded"));
    }

    fun createScreenshotFile(filename: String): File {
        val dir = "${Environment.getExternalStorageDirectory()}${File.separator}test-screenshots"
        File(dir).mkdirs() //folder name
        return File(dir, "${filename}.png")
    }
}
