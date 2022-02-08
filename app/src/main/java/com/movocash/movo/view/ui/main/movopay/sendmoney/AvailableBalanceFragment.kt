package com.movocash.movo.view.ui.main.movopay.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentAvailableBalanceBinding
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class AvailableBalanceFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentAvailableBalanceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_available_balance, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
        }
    }

}