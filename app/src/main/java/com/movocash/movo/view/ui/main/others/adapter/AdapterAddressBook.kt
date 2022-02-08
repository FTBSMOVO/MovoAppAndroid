package com.movocash.movo.view.ui.main.others.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ContactModel
import com.movocash.movo.databinding.AdapterAddressBookBinding
import com.movocash.movo.view.ui.base.ActivityBase

class AdapterAddressBook(private val mList: ArrayList<ContactModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterAddressBookBinding
    lateinit var mListener: IContactListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.adapter_address_book, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as ItemViewHolder
        mHolder.binding.model = mList[position]
        mHolder.binding.executePendingBindings()

        mHolder.binding.rlMain.setOnClickListener {
            mList.map { it.isSelected = false }
            mList[position].isSelected = true
            notifyDataSetChanged()
            mListener.onClickContact(position)
        }
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: AdapterAddressBookBinding = DataBindingUtil.bind(view)!!
    }


    fun getList(): ArrayList<ContactModel> {
        return mList
    }

    fun setMyListener(listener: IContactListener) {
        mListener = listener
    }

    interface IContactListener {
        fun onClickContact(position: Int)
    }
}

