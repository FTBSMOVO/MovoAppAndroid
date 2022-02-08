package com.movocash.movo.view.ui.main.digitalbanking.transferactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomScheduleTransferModels
import com.movocash.movo.databinding.FragmentTransferActivityDetailBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.data.model.requestmodel.MyCustomScheduleTransferModels

class TransferActivityDetailFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentTransferActivityDetailBinding

    companion object {
        lateinit var instance: TransferActivityDetailFragment
        lateinit var customobj: MyCustomScheduleTransferModels

        fun newInstance(obj1: MyCustomScheduleTransferModels): TransferActivityDetailFragment {
            instance = TransferActivityDetailFragment()
            customobj = obj1
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_activity_detail, container, false)
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = customobj
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        }
    }
}