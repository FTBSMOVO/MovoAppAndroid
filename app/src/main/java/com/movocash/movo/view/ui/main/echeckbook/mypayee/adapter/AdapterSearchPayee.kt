package com.movocash.movo.view.ui.main.echeckbook.mypayee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetMyPayeeResponseModel
import com.movocash.movo.databinding.AdapterSearchPayeeBinding

class AdapterSearchPayee(var mList: ArrayList<GetMyPayeeResponseModel.Payee>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterSearchPayeeBinding
    lateinit var mListener: ISearchPayeeListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_search_payee, parent, false)
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
        var binding: AdapterSearchPayeeBinding? = DataBindingUtil.bind(v)

    }

    fun setMyListener(listener: ISearchPayeeListener) {
        mListener = listener
    }

    interface ISearchPayeeListener {
        fun onItemClicked(position: Int)
    }
}