package com.mobdeve.ghraziellereideramos.exercise3lifecyclesp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Data for the application
    private lateinit var data: ArrayList<PostModel>

    // RecyclerView components
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter

    // NEW CODE
    private val DISPLAY_KEYS_PREFERENCE = "display_format_sharedpref"
    private val HIDE_LIKE_KEYS_PREFERENCE = "hide_like_button_sharedpref"
    private lateinit var userPreference : SharedPreferences

    // Indicators for what Layout should be used or if the like buttons should be hidden
    private val recyclerViewDefaultView = LayoutType.LINEAR_VIEW_TYPE.ordinal // int of LayoutType.LINEAR_VIEW_TYPE (default) or LayoutType.GRID_VIEW_TYPE
    private val hideLikeButtons = false // true = hide buttons; false = shown buttons (default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NEW CODE
        userPreference = getSharedPreferences("USER_PREFERENCE", MODE_PRIVATE)
        val displayFormat = userPreference.getInt(DISPLAY_KEYS_PREFERENCE, LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLikeButton = userPreference.getBoolean(HIDE_LIKE_KEYS_PREFERENCE, false)

        // Initialize the data, recyclerView and adapter
        this.data = DataHelper.initializeData()
        this.recyclerView = findViewById(R.id.recyclerView)
        this.myAdapter = MyAdapter(this.data)

        // Set the layout manager according to the default view
        this.recyclerView.layoutManager = getLayoutManager(displayFormat)

        // Initialize the view type and hide like button settings
        this.myAdapter.setViewType(displayFormat)
        this.myAdapter.setHideLikeBtn(this.hideLikeButtons)

        // Sets the adapter of the recycler view
        this.recyclerView.adapter = this.myAdapter
    }

    /*
     * Just a method to return a specific LayoutManager based on the ViewType provided.
     * */
    private fun getLayoutManager(value: Int): RecyclerView.LayoutManager {
        if (value == LayoutType.LINEAR_VIEW_TYPE.ordinal)
            return LinearLayoutManager(this)
        else
            return GridLayoutManager(this, 2)
    }

    /*
    * Responsible for inflating the options menu on the upper right corner of the screen.
    * */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /*
     * A little overkill tbh, but this method is responsible for handling the selection of items
     * in the options menu. There's only one item anyway -- Settings, which leads the user to the
     * Settings activity.
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val i = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // NEW CODE
    override fun onStop() {
        super.onStop()

        val userPreferences = userPreference.edit()
        userPreferences.putInt(DISPLAY_KEYS_PREFERENCE, myAdapter.getViewType())
        userPreferences.putBoolean(HIDE_LIKE_KEYS_PREFERENCE, myAdapter.getHideLikeBtn()!!)
        userPreferences.apply()
    }

    // NEW CODE
    override fun onResume() {
        super.onResume()

        val displayFormat = userPreference.getInt(DISPLAY_KEYS_PREFERENCE, LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLikeButton = userPreference.getBoolean(HIDE_LIKE_KEYS_PREFERENCE, false)

        val newLayoutManager = getLayoutManager(displayFormat)
        this.recyclerView.layoutManager = newLayoutManager
        this.recyclerView.adapter = myAdapter.apply {
            setViewType(displayFormat)
            setHideLikeBtn(hideLikeButton)
        }
    }
}