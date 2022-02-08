package com.movocash.movo.view.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentPasscodeBinding
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment

class PassCodeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentPasscodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_passcode, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlPassCode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlPassCode -> callFragmentWithReplace(R.id.mainContainer, PassCodeSettingsFragment(), "SideMenuFragment")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}