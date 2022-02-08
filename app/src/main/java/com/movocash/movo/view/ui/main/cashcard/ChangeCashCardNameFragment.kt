package com.movocash.movo.view.ui.main.cashcard

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.UpdateNameRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentChangeCashCardNameBinding
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.gotoHome
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.CardViewModel

class ChangeCashCardNameFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentChangeCashCardNameBinding
    private lateinit var cardsViewModel: CardViewModel

    companion object {
        lateinit var instance: ChangeCashCardNameFragment
        lateinit var cardObj: GetCardAccountsResponseModel.Card

        fun newInstance(obj: GetCardAccountsResponseModel.Card): ChangeCashCardNameFragment {
            instance = ChangeCashCardNameFragment()
            cardObj = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_cash_card_name, container, false)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        cardsViewModel.changeNameFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.changeNameResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            ActivityBase.activity.showToastMessage("Name Updated")
            gotoHome()
            callFragment(R.id.mainContainer, CashCardFragment(), null)
        })
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.etName.setText(cardObj.programAbbreviation)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (!TextUtils.isEmpty(binding.etName.text.toString())) {
                    cardsViewModel.changeCashCardName(UpdateNameRequestModel(cardObj.referenceID!!, binding.etName.text.toString()))
                } else {
                    binding.etName.requestFocus()
                    binding.etName.errorAnim(ActivityBase.activity)
                }
            }
        }
    }
}