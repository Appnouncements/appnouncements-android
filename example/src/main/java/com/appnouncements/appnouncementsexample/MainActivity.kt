package com.appnouncements.appnouncementsexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import android.widget.Toast
import com.appnouncements.sdk.Appnouncements
import com.appnouncements.sdk.Client
import com.appnouncements.sdk.support.AppnouncementsException
import com.mikepenz.actionitembadge.library.ActionItemBadge
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Client.Listener {
    private var whatsNewToolbarButton: MenuItem? = null
    private var appnouncementsClient: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Appnouncements.getClientAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        whatsNewToolbarButton = menu!!.findItem(R.id.whatsNewToolbarButton)
        setWhatsNewBadge(appnouncementsClient?.unseenReleaseNotesCount ?: 0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.whatsNewToolbarButton) {
            if (appnouncementsClient != null) {
                appnouncementsClient!!.showReleaseNotes(this)
                setWhatsNewBadge(0)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAppnouncementsClientReady(client: Client?) {
        appnouncementsClient = client
        setWhatsNewBadge(client!!.unseenReleaseNotesCount)
    }

    override fun onAppnouncementsClientFailed(error: AppnouncementsException?) {
        Toast.makeText(this, "Failed to get the appnouncements client... See logs.", Toast.LENGTH_LONG).show();
    }

    private fun setWhatsNewBadge(count: Int) {
        if (count > 0) {
            ActionItemBadge.update(this, whatsNewToolbarButton, MaterialDesignIconic.Icon.gmi_notifications, ActionItemBadge.BadgeStyles.RED, count)
        } else {
            ActionItemBadge.update(this, whatsNewToolbarButton, MaterialDesignIconic.Icon.gmi_notifications_none, null)
        }
    }
}
