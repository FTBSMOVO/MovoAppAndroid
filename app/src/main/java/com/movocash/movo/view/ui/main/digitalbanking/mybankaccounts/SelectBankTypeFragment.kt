package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentSelectBankTypeBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.Plaid.PlaidMain
import com.movocash.movo.Plaid.PlaidMainFragment
import com.movocash.movo.view.ui.main.about.AboutUsFragment

class SelectBankTypeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentSelectBankTypeBinding
    var selectionType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_bank_type, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.rlBankToCard.setOnClickListener(this)
        binding.rlCardToBank.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (selectionType != 0) {
                    proceedToAdd()
                } else {
                    ActivityBase.activity.showToastMessage("Please Select an option")
                }
            }
            R.id.rlBankToCard -> {
                selectionType = Constants.BANK_TO_CARD
               // ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, PlaidMainFragment(), null)
               // ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                /*val intent = Intent(context, PlaidMain::class.java)

               requireContext().startActivity(intent)*/
            }
            R.id.rlCardToBank -> {
                selectionType = Constants.CARD_TO_BANK
                proceedToAdd()
            }
        }
    }

    private fun proceedToAdd() {
        callFragmentWithReplace(R.id.mainContainer, AddBankAccountFragment.newInstance(selectionType,false, null), "AddBankAccountFragment")
    }
}