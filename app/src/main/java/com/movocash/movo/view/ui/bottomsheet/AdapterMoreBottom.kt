package com.movocash.movo.view.ui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.databinding.AdapterMoreBottomBinding

class AdapterMoreBottom(val context: Context, private val mList: ArrayList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterMoreBottomBinding
    private lateinit var mListener: IMoreClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_more_bottom, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as ItemViewHolder
        mHolder.binding.isSepratorShown = position != mList!!.size - 1
        mHolder.binding.model = mList!![position]
        mHolder.binding.executePendingBindings()
        mHolder.binding.rlMain.setOnClickListener {
            mListener.onClickItem(position)
        }

    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: AdapterMoreBottomBinding = DataBindingUtil.bind(view)!!
    }

    fun getList(): ArrayList<String>? {
        return mList
    }

    fun setMyListener(listener: IMoreClickListener) {
        mListener = listener
    }

    interface IMoreClickListener {
        fun onClickItem(position: Int)
    }
}