package com.movocash.movo.view.ui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import kotlinx.android.synthetic.main.adapter_bottom_selector_item.view.*
import java.util.ArrayList

class AdapterBottomSheet(val context: Context, var mlist: ArrayList<String>) : RecyclerView.Adapter<AdapterBottomSheet.ItemViewHolder>() {

    private lateinit var mListener: IGenderClickListener

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.tvItem.text = mlist[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ItemViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.adapter_bottom_selector_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlist.size
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem = view.tvItem!!
    }


    fun setMyListener(listner: IGenderClickListener) {
        mListener = listner
    }

    interface IGenderClickListener {
        fun onClick(position: Int)

    }
}