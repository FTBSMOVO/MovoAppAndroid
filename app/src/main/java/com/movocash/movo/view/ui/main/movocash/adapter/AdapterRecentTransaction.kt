package com.movocash.movo.view.ui.main.movocash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetMiniStatementResponseModel
import com.movocash.movo.databinding.AdapterRecentTransactionBinding

class AdapterRecentTransaction(var mList:ArrayList<GetMiniStatementResponseModel.Transaction>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterRecentTransactionBinding
    lateinit var mListener: ITransaction

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_recent_transaction, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        var size = 0
        size = if (mList.size> 5) 5 else mList.size
        return size
    }

    override fun onBindViewHolder(mHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = mHolder as ItemViewHolder
        holder.binding!!.model = mList[position]
        holder.binding!!.executePendingBindings()

        holder.binding!!.rlMain.setOnClickListener {
            mListener.onItemClicked(position)
        }
    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: AdapterRecentTransactionBinding? = DataBindingUtil.bind(v)

    }

    fun setMyListener(listener: ITransaction) {
        mListener = listener
    }

    interface ITransaction {
        fun onItemClicked(position: Int)
    }
}