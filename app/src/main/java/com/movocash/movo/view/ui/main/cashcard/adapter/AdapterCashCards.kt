package com.movocash.movo.view.ui.main.cashcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.AdapterCashCardBinding
import com.movocash.movo.utilities.Constants

class AdapterCashCards(var mList: List<GetCardAccountsResponseModel.Card> = ArrayList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterCashCardBinding
    lateinit var mListener: ITransaction
    private val actualList = mList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_cash_card, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(mHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = mHolder as ItemViewHolder

        holder.binding!!.model = mList[position]
        /*if(mList[position].statusCode == "F") {
            holder.binding!!.model = mList[position]
        }*/

        holder.binding!!.executePendingBindings()
            holder.binding!!.rlMain.setOnClickListener {
                mListener.onItemClicked(position,mList)
            }
    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: AdapterCashCardBinding? = DataBindingUtil.bind(v)
    }

    fun setMyListener(listener: ITransaction) {
        mListener = listener
    }

    interface ITransaction {
        fun onItemClicked(position: Int,sList: List<GetCardAccountsResponseModel.Card>)
    }

    fun filter(keywords: String){
        mList = actualList.filter { it.programAbbreviation!!.toLowerCase()!!.contains(keywords.toLowerCase()) }
        notifyDataSetChanged()
    }
}