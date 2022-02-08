package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.databinding.ActivityAdapterBankAccountsBinding
import com.movocash.movo.databinding.AdapterMyBankAccountsBinding


class AdapterBankAccounts(var mList: ArrayList<GetBankAccountsResponseModel.Account>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: ActivityAdapterBankAccountsBinding
    lateinit var mListener: IBankAccounts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.activity_adapter_bank_accounts, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(mHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = mHolder as ItemViewHolder
        holder.binding!!.model = mList[position]
        holder.binding!!.executePendingBindings()

        holder.binding!!.ivDeleteAccount.setOnClickListener {
            mListener.onItemClicked(position)
        }

        holder.binding!!.ivtransfers.setOnClickListener {
            mListener.onTransfer(position)
        }
    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: ActivityAdapterBankAccountsBinding? = DataBindingUtil.bind(v)

    }

    fun setMyListener(listener: IBankAccounts) {
        mListener = listener
    }

    interface IBankAccounts {
        fun onItemClicked(position: Int)

        fun onTransfer(position: Int)

    }
}