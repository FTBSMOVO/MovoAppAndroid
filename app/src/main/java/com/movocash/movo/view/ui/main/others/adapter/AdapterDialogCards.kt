package com.movocash.movo.view.ui.main.others.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.AdapterDialogCardBinding

class AdapterDialogCards(private var mList: ArrayList<GetCardAccountsResponseModel.Card>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterDialogCardBinding
    lateinit var mListener: IDialogCardsListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_dialog_card, parent, false)
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
            mList.map { it.isSelected = false }
            mList[position].isSelected = true
            notifyDataSetChanged()
            mListener.onClickCard(position)
        }

    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: AdapterDialogCardBinding? = DataBindingUtil.bind(v)
    }

    fun setMyListener(listener: IDialogCardsListener) {
        mListener = listener
    }

    interface IDialogCardsListener {
        fun onClickCard(position: Int)
    }

}