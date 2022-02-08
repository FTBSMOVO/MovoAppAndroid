package com.movocash.movo.view.ui.main.movocash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.AdapterCardsBinding
import com.movocash.movo.databinding.AdapterCardsHeaderBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.copyText
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase

class AdapterMyCards(var mList: ArrayList<GetCardAccountsResponseModel.Card>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: AdapterCardsBinding
    lateinit var headerBinding: AdapterCardsHeaderBinding
    lateinit var mListener: ICardListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                headerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_cards_header, parent, false)
                HeaderViewHolder(headerBinding.root)
            }
            1 -> {
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_cards, parent, false)
                ItemViewHolder(binding.root)
            }
            else -> {
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_cards, parent, false)
                ItemViewHolder(binding.root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mList[position].type) {
            Constants.CONST_PRIMARY -> 0
            Constants.CONST_SECONDARY -> 1
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(mHolder: RecyclerView.ViewHolder, position: Int) {

        when (mList[position].type) {
            0 -> {
                val holder = mHolder as HeaderViewHolder
                holder.binding!!.model = mList[position]
                holder.binding!!.executePendingBindings()

                holder.binding!!.rlCard.setOnClickListener {
                    mListener.onClickCard(position)
                }
                /*holder.binding!!.btnGooglePay.setOnClickListener {
                    mListener.googlePayImplementation(position)
                }*/

                holder.binding!!.btnViewCard.setOnClickListener {
//                    holder.binding!!.btnViewCard.visibility = View.GONE
//                    holder.binding!!.elCard.expand()
                    mListener.onClickViewCard()
                }


              /*  holder.binding!!.btnHideCard.setOnClickListener {
//                    holder.binding!!.elCard.collapse()
//                    holder.binding!!.btnViewCard.visibility = View.VISIBLE
                }

                holder.binding!!.btnCopyNum.setOnClickListener {
                    ActivityBase.activity.showToastMessage("Card Number Copied")
                    ActivityBase.activity.copyText(mList[position].cardNumber!!)
                }*/
            }
            1 -> {
                val holder = mHolder as ItemViewHolder
                holder.binding!!.model = mList[position]
                holder.binding!!.executePendingBindings()

                holder.binding!!.rlCard.setOnClickListener {
                    mListener.onClickCard(position)
                }
                /*holder.binding!!.btnGooglePay2.setOnClickListener {
                    mListener.googlePayImplementation(position)
                }*/
            }
            else -> {
                val holder = mHolder as ItemViewHolder
                holder.binding!!.model = mList[position]
                holder.binding!!.executePendingBindings()

                holder.binding!!.rlCard.setOnClickListener {
                    mListener.onClickCard(position)
                }
            }
        }
    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: AdapterCardsBinding? = DataBindingUtil.bind(v)
    }

    inner class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: AdapterCardsHeaderBinding? = DataBindingUtil.bind(v)
    }

    fun setMyListener(listener: ICardListener) {
        mListener = listener
    }

    interface ICardListener {
        fun onClickCard(position: Int)
        fun onClickViewCard()
        fun googlePayImplementation(position: Int)

    }
}