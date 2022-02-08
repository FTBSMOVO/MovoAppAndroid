package com.movocash.movo.view.ui.main.deposithub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentDepositWebBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class DepositWebFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentDepositWebBinding

    companion object {
        lateinit var instance: DepositWebFragment
        var type: Int = 0

        fun newInstance(t: Int): DepositWebFragment {
            instance = DepositWebFragment()
            type = t
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deposit_web, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
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