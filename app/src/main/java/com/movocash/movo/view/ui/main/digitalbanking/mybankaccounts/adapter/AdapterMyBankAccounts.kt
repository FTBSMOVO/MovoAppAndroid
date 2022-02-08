package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.databinding.AdapterMyBankAccountsBinding


class AdapterMyBankAccounts(var mList: ArrayList<GetBankAccountsResponseModel.Account>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterMyBankAccountsBinding
    lateinit var mListener: IBankAccounts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_my_bank_accounts, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
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
        var binding: AdapterMyBankAccountsBinding? = DataBindingUtil.bind(v)

    }

    fun setMyListener(listener: IBankAccounts) {
        mListener = listener
    }

    interface IBankAccounts {
        fun onItemClicked(position: Int)
    }
}