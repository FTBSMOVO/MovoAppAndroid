package com.movocash.movo.view.ui.main.movocash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomHistoryModel
import com.movocash.movo.databinding.AdapterCardTransactionsBinding
import com.movocash.movo.databinding.AdapterCardTransactionsDateBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.utils.DateUtil

class AdapterCardTransactions(private val mList: ArrayList<CustomHistoryModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemBinding: AdapterCardTransactionsBinding
    lateinit var dateBinding: AdapterCardTransactionsDateBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_card_transactions, parent, false)
                ItemViewHolder(itemBinding.root)
            }
            1 -> {
                dateBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_card_transactions_date, parent, false)
                ItemDateViewHolder(dateBinding.root)
            }
            else -> {
                itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_card_transactions, parent, false)
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
        var binding: AdapterCardTransactionsBinding = DataBindingUtil.bind(view)!!

    }

    class ItemDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterCardTransactionsDateBinding = DataBindingUtil.bind(view)!!

        fun bindWithData(item: CustomHistoryModel) {
            binding.tvDate.text = DateUtil.convertSingleDateChat(item.date + "T00:00:00")
        }

    }
}