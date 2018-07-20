package com.appnouncements.appnouncementsexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.VISIBLE
import android.widget.Toast
import com.appnouncements.sdk.Appnouncements
import com.appnouncements.sdk.Client
import com.appnouncements.sdk.support.AppnouncementsException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Client.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Appnouncements.getClientAsync(this)
    }

    override fun onAppnouncementsClientReady(client: Client?) {
        releaseNotesButton.setOnClickListener { client!!.showReleaseNotes(this) }
        releaseNotesButton.setText("Show Release Notes (${client!!.unseenReleaseNotesCount})")
        releaseNotesButton.visibility = VISIBLE
    }

    override fun onAppnouncementsClientFailed(error: AppnouncementsException?) {
        Toast.makeText(this, "Failed to get the appnouncements client... See logs.", Toast.LENGTH_LONG).show();
    }
}
