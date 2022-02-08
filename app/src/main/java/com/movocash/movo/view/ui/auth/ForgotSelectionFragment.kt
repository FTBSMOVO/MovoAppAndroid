package com.movocash.movo.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentSelectionBinding
import com.movocash.movo.view.ui.base.ActivityBase

class ForgotSelectionFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentSelectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selection, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.rlBack.setOnClickListener(this)
        binding.btnUserName.setOnClickListener(this)
        binding.btnPass.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnUserName -> addFragment(R.id.authContainer, SendCodeFragment.newInstance(true), null)
            R.id.btnPass -> addFragment(R.id.authContainer, SendCodeFragment.newInstance(false), null)
        }
    }
}