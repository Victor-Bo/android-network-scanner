package com.victorb.androidnetworkscanner

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnCancel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    private var animator: ObjectAnimator? = null

    private var resultsAdapter = ResultsAdapter()
    private lateinit var scanningJob: Job
    private lateinit var scanner: Scanner

    /**
     * The main function
     * Sets up the recycler view, initializes WiFi and starts the scan
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Default behaviour
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)

        // Set the LayoutManager and the Adapter for RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview_results)
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = resultsAdapter

        // Start a scan
        // The delay is necessary to find the toolbar refresh button
        // See : https://stackoverflow.com/questions/28840815/menu-item-animation-rotate-indefinitely-its-custom-icon
        runOnMainThreadDelayed(100) {
            val view = findViewById<View>(R.id.action_refresh)
            animator = ObjectAnimator.ofFloat(view, "rotation", 360f).apply {
                duration = 1000
                repeatCount = Animation.INFINITE
                interpolator = LinearInterpolator()
                doOnCancel {
                    view.rotation = 0f
                }
            }
            scanner = Scanner(this, resultsAdapter, animator)
            scanner.startScan()
        }
    }

    /**
     * Define actions for toolbar buttons
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Control menu buttons (toolbar buttons)
        when (item.itemId) {
            // When refresh menu button clicked
            R.id.action_refresh -> {
                resultsAdapter.clear()
                scanner.startScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Sets the menu to use for the toolbar buttons
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Set the menu (actually the refresh button)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}