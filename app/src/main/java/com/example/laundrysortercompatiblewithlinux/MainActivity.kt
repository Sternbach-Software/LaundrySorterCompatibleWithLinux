package com.example.laundrysortercompatiblewithlinux

import LaundryItem
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundrysortercompatiblewithlinux.KotlinFunctionLibrary.print

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar.print()
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity concreate run")
        val recyclerView = findViewById<RecyclerView?>(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = CustomAdapter(
            listOf(LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),LaundryItem(),)
        )
    }
}