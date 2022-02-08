package com.movocash.movo.view.ui.main.digitalbanking

import android.R.attr
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentDirectDepositBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.movocash.movo.utilities.extensions.showToastMessage


class DirectDepositFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentDirectDepositBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_deposit, container, false)
        initViews()
        setListeners()
        return binding.root
    }

    private fun initViews() {
        binding.tvTitle.isSelected = true
        binding.depoAccount = MovoApp.db.getString(Constants.CUSTOMER_ID)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.btnClipboard.setOnClickListener(this)
        binding.btnClipboard1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.btn_clipboard -> {

                val myClipboard: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("simple text", "125109019")
                ActivityBase.activity.showToastMessage("Copied")

                myClipboard.setPrimaryClip(clip)
            }
            R.id.btn_clipboard1 -> {

                val myClipboard: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("simple text", MovoApp.db.getString(Constants.CUSTOMER_ID))
                ActivityBase.activity.showToastMessage("Copied")

                myClipboard.setPrimaryClip(clip)
            }
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