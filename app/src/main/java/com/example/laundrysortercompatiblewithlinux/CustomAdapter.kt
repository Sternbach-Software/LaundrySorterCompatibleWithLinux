package com.example.laundrysortercompatiblewithlinux

import LaundryItem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

private const val TAG = "CustomAdapter"

class CustomAdapter(private val mDataSet: List<LaundryItem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView1: TextView
        val textView2: TextView
        val textView3: TextView
        val textView4: TextView
        val textView5: TextView
        val textView6: TextView
        val textView7: TextView
        val textView8: TextView

        init {
            v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
            val card1 = v.findViewById<MaterialCardView>(R.id.card1)
            val card2 = v.findViewById<MaterialCardView>(R.id.card2)
            val card3 = v.findViewById<MaterialCardView>(R.id.card3)
            val card4 = v.findViewById<MaterialCardView>(R.id.card4)
            val card5 = v.findViewById<MaterialCardView>(R.id.card5)
            val card6 = v.findViewById<MaterialCardView>(R.id.card6)
            val card7 = v.findViewById<MaterialCardView>(R.id.card7)
            val card8 = v.findViewById<MaterialCardView>(R.id.card8)
            textView1 = card1.findViewById(R.id.text_view)
            textView2 = card2.findViewById(R.id.text_view)
            textView3 = card3.findViewById(R.id.text_view)
            textView4 = card4.findViewById(R.id.text_view)
            textView5 = card5.findViewById(R.id.text_view)
            textView6 = card6.findViewById(R.id.text_view)
            textView7 = card7.findViewById(R.id.text_view)
            textView8 = card8.findViewById(R.id.text_view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        val laundryItem = mDataSet[position]
        viewHolder.textView1.text = laundryItem.ArticleName
    }

    override fun getItemCount(): Int = mDataSet.size
}