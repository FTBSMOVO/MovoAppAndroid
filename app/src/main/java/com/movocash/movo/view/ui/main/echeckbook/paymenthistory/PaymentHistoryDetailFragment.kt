package com.movocash.movo.view.ui.main.echeckbook.paymenthistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.databinding.FragmentPaymentHistoryDetailBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class PaymentHistoryDetailFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentPaymentHistoryDetailBinding

    companion object {
        lateinit var instance: PaymentHistoryDetailFragment
        lateinit var model: CustomPaymentDetailModel

        fun newInstance(obj: CustomPaymentDetailModel): PaymentHistoryDetailFragment {
            instance = PaymentHistoryDetailFragment()
            model = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_history_detail, container, false)
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = model
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