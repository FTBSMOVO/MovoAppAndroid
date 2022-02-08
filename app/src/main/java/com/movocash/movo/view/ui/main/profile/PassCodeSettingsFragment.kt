package com.movocash.movo.view.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentPasscodeSettingsBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class PassCodeSettingsFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentPasscodeSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_passcode_settings, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlChangePassCode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlChangePassCode -> callFragmentWithReplace(R.id.mainContainer, ChangePassCodeFragment(), "SideMenuFragment")
        }
    }

}