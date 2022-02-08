package com.movocash.movo.view.ui.main.movopay.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomShareHistoryModel
import com.movocash.movo.databinding.AdapterHistoryBinding
import com.movocash.movo.databinding.AdapterHistoryDateBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.utils.DateUtil

class AdapterHistory(private val mList: ArrayList<CustomShareHistoryModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemBinding: AdapterHistoryBinding
    lateinit var dateBinding: AdapterHistoryDateBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_history, parent, false)
                ItemViewHolder(itemBinding.root)
            }
            1 -> {
                dateBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_history_date, parent, false)
                ItemDateViewHolder(dateBinding.root)
            }
            else -> {
                itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_history, parent, false)
                ItemViewHolder(itemBinding.root)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (mList[position].type) {
            0 -> {
                val mItemHolder = holder as ItemViewHolder
                mItemHolder.binding.model = mList[position]
            }
            1 -> {
                val mDateHolder = holder as ItemDateViewHolder
                mDateHolder.bindWithData(mList[position])

            }
            else -> {
                val mItemHolder = holder as ItemViewHolder
                mItemHolder.binding.model = mList[position]
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mList[position].type) {
            Constants.CONST_TEXT -> 0
            Constants.CONST_DATE -> 1
            else -> 0
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterHistoryBinding = DataBindingUtil.bind(view)!!
    }

    class ItemDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterHistoryDateBinding = DataBindingUtil.bind(view)!!

        fun bindWithData(item: CustomShareHistoryModel) {
            binding.tvDate.text = DateUtil.convertSingleDateChat(item.date + "T00:00:00")
        }

    }
}