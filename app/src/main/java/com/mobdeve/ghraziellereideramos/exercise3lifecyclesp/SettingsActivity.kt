package com.mobdeve.ghraziellereideramos.exercise3lifecyclesp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    // Views for the switches
    private lateinit var linearViewSwitch: Switch
    private lateinit var hideLikeSwitch: Switch

    // NEW CODE
    private val DISPLAY_KEYS_PREFERENCE = "display_format_sharedpref"
    private val HIDE_LIKE_KEYS_PREFERENCE = "hide_like_button_sharedpref"
    private lateinit var userPreference : SharedPreferences

    // Indicators for what Layout should be used or if the like buttons should be hidden
    private val viewSelected = LayoutType.LINEAR_VIEW_TYPE.ordinal // int of LayoutType.LINEAR_VIEW_TYPE (default) or LayoutType.GRID_VIEW_TYPE
    private val hideLikeSelected = false // true -> hidden buttons; false -> shown buttons (default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // NEW CODE
        userPreference = getSharedPreferences("USER_PREFERENCE", MODE_PRIVATE)
        val displayFormat = userPreference.getInt(DISPLAY_KEYS_PREFERENCE, LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLikeButton = userPreference.getBoolean(HIDE_LIKE_KEYS_PREFERENCE, true)

        // Instantiation of the Switch views
        this.linearViewSwitch = findViewById(R.id.viewSwitch)
        this.hideLikeSwitch = findViewById(R.id.hideLikeSwitch)

        linearViewSwitch.isChecked = (displayFormat == LayoutType.LINEAR_VIEW_TYPE.ordinal)
        hideLikeSwitch.isChecked = hideLikeButton
    }

    // NEW CODE
    override fun onPause() {
        super.onPause()

        val userPreferences = userPreference.edit()
        userPreferences.putInt(DISPLAY_KEYS_PREFERENCE, returnInt(linearViewSwitch.isChecked))
        userPreferences.putBoolean(HIDE_LIKE_KEYS_PREFERENCE, hideLikeSwitch.isChecked)
        userPreferences.apply()
    }

    // Added
    override fun onStop() {
        super.onStop()

        val userPreferences = userPreference.edit()
        userPreferences.putInt(DISPLAY_KEYS_PREFERENCE, returnInt(linearViewSwitch.isChecked))
        userPreferences.putBoolean(HIDE_LIKE_KEYS_PREFERENCE, hideLikeSwitch.isChecked)
        userPreferences.apply()
    }

    /*
     * Method to map an integer to the appropriate boolean value based on the given LayoutType.
     * */
    private fun returnBoolean(value: Int): Boolean {
        if (value == LayoutType.LINEAR_VIEW_TYPE.ordinal)
            return true
        else
            return false
    }

    /*
     * Method to map a boolean value (representing whether the linearViewSwitch is checked or not)
     * to appropriate LayoutType in ordinal representation.
     * */
    private fun returnInt(value: Boolean): Int {
        if (value == true)
            return LayoutType.LINEAR_VIEW_TYPE.ordinal
        else
            return LayoutType.GRID_VIEW_TYPE.ordinal
    }
}