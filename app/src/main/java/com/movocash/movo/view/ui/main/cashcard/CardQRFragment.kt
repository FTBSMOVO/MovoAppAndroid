package com.movocash.movo.view.ui.main.cashcard

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentCardQrBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class CardQRFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentCardQrBinding

    companion object {
        lateinit var instance: CardQRFragment
        var cardImage: Bitmap? = null
        var balance: Double = 0.0

        fun newInstance(image: Bitmap, amount: Double): CardQRFragment {
            instance = CardQRFragment()
            cardImage = image
            balance = amount
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_qr, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        setViews()
        return binding.root
    }

    private fun setViews() {
        binding.ivCard.setImageBitmap(cardImage)
        binding.balance = balance
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.btnShowCard.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnShowCard -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        }
    }
}